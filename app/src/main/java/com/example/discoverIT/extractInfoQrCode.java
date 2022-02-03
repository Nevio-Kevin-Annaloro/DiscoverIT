package com.example.discoverIT;

public class extractInfoQrCode {

    private String code = "";

    public extractInfoQrCode(String c){
        code = c;
    }

    public String findProvince(String codec){
        if(codec.isEmpty()){
            return "Disco-Quiz";
        }else if(codec.equals("FTKO36-AL")){
            return "Duomo (AL)";
        }else if(codec.equals("GLPO21-AL")){
            return "Cittadella (AL)";
        }else if(codec.equals("CRTO43-AL")){
            return "Meier bridge (AL)";
        }else if(codec.equals("XLGF56-AL")){
            return "Bollente (AL)";
        }else if(codec.equals("KZAS82-AL")){
            return "Acquedotto Romano (AL)";
        }else if(codec.equals("QDMV01-AL")){
            return "Castello Paleologi (AL)";
        }else if(codec.equals("MELC02-AL")){
            return "Villa Ottolenghi (AL)";
        }else{
            return "Disco-Quiz";
        }
    }

}
