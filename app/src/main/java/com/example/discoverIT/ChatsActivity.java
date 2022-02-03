package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.discoverIT.Constants.Constants;
import com.example.discoverIT.adapters.ChatAdapter;
import com.example.discoverIT.databinding.ActivityChatBinding;
import com.example.discoverIT.databinding.ActivityChatsBinding;
import com.example.discoverIT.models.ChatMessage;
import com.example.discoverIT.models.User;
import com.example.discoverIT.network.ApiClient;
import com.example.discoverIT.network.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicMarkableReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Objects.*;

public class ChatsActivity extends BaseActivity {

    private ActivityChatsBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseFirestore databasecollection;
    private String conversationId = null;
    private String nameUID = null;
    private String imageUID = null;
    private String tokenUID = null;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemRef;
    private Boolean isReceiverAvailable = false;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private String encodedImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.KEY_RECEIVER_ID="receiverId";

        initDB(new FirebaseCallback() {
            @Override
            public void onCallback(String[] informations) {
                nameUID = informations[0];
                imageUID = informations[1];
                tokenUID = informations[2];
                Log.d("DATABASE", "NOME callback: "+nameUID);
            }
        });

        setListeners();
        loadReceiverDetails();
        init();
        Log.d("DATABASE", "IMAGE USER RECEIVER: "+receiverUser.image);
        listenMessages();
        listenAvailabilityOfReceiver(new FirebaseAvailabilityCallback() {
            @Override
            public void onAvailabilityCallback(int available) {
                isReceiverAvailable = available == 1;
                if (isReceiverAvailable) {
                    binding.textAvailability.setVisibility(View.VISIBLE);
                } else {
                    binding.textAvailability.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initDB(FirebaseCallback firebaseCallback) {
        Constants.KEY_RECEIVER_ID="receiverId";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        String[] infos = new String[3];
        itemRef = firebaseRootRef.child("Utenti/"+uid);
        Log.d("DATABASE", "fuori ondatachange");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DATABASE", "Dentro ondatachange");

                String nome = snapshot.child("name").getValue(String.class);
                String pic = snapshot.child("photo").getValue(String.class);
                String tok = snapshot.child("token").getValue(String.class);

                Log.d("DATABASE", "NAME: "+nome);

                infos[0]=nome;
                infos[1]=pic;
                infos[2]=tok;
                firebaseCallback.onCallback(infos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private void sendMessage() {
        Constants.KEY_RECEIVER_ID="receiverId";
        Log.d("DATABASE", "input: " + binding.inputMessage.getText().toString());
        if(binding.inputMessage.getText().toString().equals("")){
            Log.d("DATABASE", "IF se nulllllllllllllllll: " + binding.inputMessage.getText());
            Toast.makeText(this, "Invalid input", Toast.LENGTH_LONG).show();
        } else {
            initDB(new FirebaseCallback() {
                @Override
                public void onCallback(String[] informations) {
                    nameUID = informations[0];
                    imageUID = informations[1];
                    tokenUID = informations[2];
                    Log.d("DATABASE", "NOME callback: "+nameUID);
                }
            });

            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, uid);
            Log.d("DATABASE", "SENDER ID chats: "+Constants.KEY_SENDER_ID);
            Log.d("DATABASE", "SENDER ID user: "+uid);
            Constants.KEY_RECEIVER_ID="receiverId";
            message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            Log.d("DATABASE", "RECEIVER ID chats: "+Constants.KEY_RECEIVER_ID);
            Log.d("DATABASE", "RECEIVER ID user: "+receiverUser.id);
            message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
            message.put(Constants.KEY_TIMESTAMP, new Date());
            databasecollection.collection(Constants.KEY_COLLECTION_CHAT)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("DATABASE", "user added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("DATABASE", "Error adding user", e);
                        }
                    });
            ;
            Log.d("DATABASE", "PRIMA IF: " + nameUID);
            if (conversationId != null) {
                Log.d("DATABASE", "DENTRO IF: " + nameUID);
                updateConversion(binding.inputMessage.getText().toString());
            } else {
                Log.d("DATABASE", "DENTRO ELSE: " + nameUID);
                HashMap<String, Object> conversion = new HashMap<>();
                conversion.put(Constants.KEY_SENDER_ID, uid);
                conversion.put(Constants.KEY_SENDER_NAME, nameUID);
                Log.d("DATABASE", "NAMEUID ULTIMO: " + nameUID);
                conversion.put(Constants.KEY_SENDER_IMAGE, imageUID);
                conversion.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
                conversion.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
                conversion.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.image);
                conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
                conversion.put(Constants.KEY_TIMESTAMP, new Date());
                addConversion(conversion);
            }
            if (!isReceiverAvailable) {
                try {
                    JSONArray tokens = new JSONArray();
                    tokens.put(receiverUser.token);

                    JSONObject data = new JSONObject();
                    data.put(Constants.KEY_USER_ID, uid);
                    data.put(Constants.KEY_NAME, nameUID);
                    data.put(Constants.KEY_FCM_TOKEN, tokenUID);
                    data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());

                    JSONObject body = new JSONObject();
                    body.put(Constants.REMOTE_MSG_DATA, data);
                    body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                    sendNotification(body.toString());
                } catch (Exception exception) {
                    showToast(exception.getMessage());
                }
            }
            binding.inputMessage.setText(null);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String messageBody) {
        if(Constants.SEND_NOTIFICATION){
            Log.d("DATABASE", "MANDARE NOTIFICHE: " + Constants.SEND_NOTIFICATION);
        }else {
            Log.d("DATABASE", "MANDARE NOTIFICHE: " + Constants.SEND_NOTIFICATION);
        }
        Constants.KEY_RECEIVER_ID="receiverId";
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showToast("Notification sent successfully");
                } else {
                    showToast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void listenAvailabilityOfReceiver(FirebaseAvailabilityCallback firebaseAvailabilityCallback) {
        Constants.KEY_RECEIVER_ID="receiverId";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        itemRef = firebaseRootRef.child("Utenti/"+receiverUser.id);
        Log.d("DATABASE", "fuori ondatachange utente receiver: "+receiverUser.id);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DATABASE", "Dentro ondatachange");


                //int availabilities = Objects.<Long>requireNonNull(
                //        snapshot.child("availability").getValue(Long.class)
                //).intValue();

                int availabilities = 0;

                if(snapshot.child("availability").exists()){
                    availabilities = Objects.requireNonNull(snapshot.child("availability").getValue(Integer.class));
                } else {
                    availabilities = 0;
                }

                Log.d("DATABASE", "availability: "+availabilities);

                receiverUser.token = snapshot.child("token").getValue(String.class);
                if (receiverUser.image == null) {
                    receiverUser.image = snapshot.child("photo").getValue(String.class);;
                    chatAdapter.setReceiverProfileImage(getBitmapFromEncodedString(receiverUser.image));
                    chatAdapter.notifyItemRangeChanged(0, chatMessages.size());
                }

                firebaseAvailabilityCallback.onAvailabilityCallback(availabilities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addValueEventListener(valueEventListener);
    }

    private  void listenMessages() {
        Constants.KEY_RECEIVER_ID="receiverId";
        databasecollection.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, uid)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);
        databasecollection.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, uid)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        Constants.KEY_RECEIVER_ID="receiverId";
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversationId == null) {
            checkForConversion();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        Constants.KEY_RECEIVER_ID="receiverId";
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                uid,
                getBitmapFromEncodedString(receiverUser.image)
        );
        Log.d("DATABASE", "image receiverUser: "+receiverUser.image);
        databasecollection = FirebaseFirestore.getInstance();
        binding.chatRecyclerView.setAdapter(chatAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.getMimeDecoder().decode(encodedImage);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
        Log.d("DATABASE", "idddddddddd: "+receiverUser.id);

        getImage(new FirebaseImageCallback() {
            @Override
            public void onImageCallback(String image) {
                receiverUser.image = image;
            }
        });

        Log.d("DATABASE", "imageeeeeeee: "+receiverUser.image);
        //Constants.KEY_RECEIVER_ID = receiverUser.id;
        Constants.KEY_USER_INFO = receiverUser.id;
    }

    private void setListeners() {

        initDB(new FirebaseCallback() {
            @Override
            public void onCallback(String[] informations) {
                nameUID = informations[0];
                imageUID = informations[1];
                Log.d("DATABASE", "NOME callback: "+nameUID);
            }
        });

        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
        binding.imageInfo.setOnClickListener(v -> startActivity(new Intent(ChatsActivity.this,UserInfo.class)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChatsActivity.this,HomeActivity.class));
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion) {
        databasecollection.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversion(String message) {
        DocumentReference documentReference =
                databasecollection.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversationId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    private void checkForConversion() {
        if (chatMessages.size() != 0) {
            checkForConversionRemotely(
                    uid,
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    uid
            );
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId) {
        databasecollection.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

    private interface FirebaseCallback {
        void onCallback(String[] informations);
    }

    private interface FirebaseAvailabilityCallback {
        void onAvailabilityCallback(int available);
    }

    private interface FirebaseImageCallback {
        void onImageCallback(String image);
    }

    void getImage(FirebaseImageCallback firebaseImageCallback) {
        myRef = database.getReference("Utenti/"+receiverUser.id+"/photo");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                encodedImage = value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        firebaseImageCallback.onImageCallback(encodedImage);
    }

    @Override
    protected void onResume() {
        listenAvailabilityOfReceiver(new FirebaseAvailabilityCallback() {
            @Override
            public void onAvailabilityCallback(int available) {
                isReceiverAvailable = available == 1;
                if (isReceiverAvailable) {
                    binding.textAvailability.setVisibility(View.VISIBLE);
                } else {
                    binding.textAvailability.setVisibility(View.GONE);
                }
            }
        });
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.getMimeDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}