package com.example.discoverIT;

public class History {

    private String history;

    public History(String h){
        history = h;
    }

    public String findHistory(String codec){
        if(codec.isEmpty()){
            return "An error occured. \nPlease Reload the app!";
        }else if(codec.equals("FTKO36-AL")){
            return "The earliest records date back to the second half of the 12th century, when the " +
                    "first cathedral church dedicated to St. Peter the Apostle was built between 1170 " +
                    "and 1175. The church was soon too small so substantial enlargement interventions " +
                    "were carried out towards the end of the 13th century. The ancient cathedral resisted " +
                    "until the early nineteenth century, when it was demolished by order of Napoleon " +
                    "between February and July 1803. A new church was built in the thirteenth century " +
                    "and was owned by the Dominicans, until in 1797 it was confiscated by the French " +
                    "occupiers and transformed into their headquarters. The building therefore needed a " +
                    "laborious restoration work: a neoclassical church came out of it, with the almost " +
                    "total disappearance of the gothic of the previous church. The building was consecrated " +
                    "on July 4, 1879. The church was devastated by a fire in September 1925: the work " +
                    "that followed, completed in 1929, led to the total refurbishment of the interior " +
                    "decoration by Luigi Morgari.";
        }else if(codec.equals("GLPO21-AL")){
            return "The Citadel of Alessandria is the only plain fortress built by the Savoy in the " +
                    "18th century and is the only European fortress still inserted in its original environmental " +
                    "context. it consists of six bastioned fronts equipped with knights, connected by thick " +
                    "rectilinear curtains and crossed by tunnels and casemates. an immense fortress that extends" +
                    " over about 60 hectares whose longest side is parallel to the axis of the river. " +
                    "The construction of the Citadel of Alessandria was part of a vast defense program of " +
                    "the Savoy State which included a system of forts to block the Alpine accesses to the plain. " +
                    "The Citadel is located north-west of the city of Alessandria on the left bank of the river Tanaro. " +
                    "It is the lowest area of Piedmont at about 90 meters above sea level. " +
                    "Vittorio Amedeo II entrusted the task of designing the Citadel to Ignazio Bertola.";
        }else if(codec.equals("CRTO43-AL")){
            return "Meier Bridge. Even before the foundation of Alessandria, a wooden bridge connected two of the " +
                    "villages that later gave life to the city: the village of Rovereto and the village of Bergoglio." +
                    "The bridge currently joins the two city banks of the Tanaro river and connects the city with the 18th century Citadel." +
                    "Work on the new bridge, designed by Richard Meier, lasted 4 years, from 2012 to 2016. " +
                    "The new bridge offers separate parallel paths for pedestrian and vehicular traffic. " +
                    "The pedestrian walkway becomes a \"public square\" where Alexandria can find a new and positive relationship with the river.";
        }else if(codec.equals("XLGF56-AL")){
            return "La Bollente is located in the center of Bollente’s square, on the side of the central Corso Italia." +
                    " It is an octagonal marble aedicule, built in 1879 by the architect Giovanni Ceruti. " +
                    "This surrounds a thermal spring from which boiling and healing water flows: " +
                    "560 liters per minute at 74.5 ° C of sulphurous-salty-bromine-iodine water. " +
                    "An ancient tradition tells that newborn children were brought to the spring to be immersed " +
                    "in it for a moment: if they came out alive, they deserved the nickname of Sgaientò, that is, burned. " +
                    "La Bollente is the symbol of the city as an emblem of its ancient history as a spa.";
        }else if(codec.equals("KZAS82-AL")){
            return "The arches of the Roman aqueduct rise just outside the town of Acqui Terme along the right bank" +
                    " of the Bormida river and constitute one of the most characteristic elements of the Acqui landscape. " +
                    "The construction of the Roman aqueduct can be traced back to the early imperial age, perhaps even" +
                    "to the Augustan era (early 1st century AD). There are currently two large sections of the original structure, " +
                    "composed respectively of seven and eight masonry pillars, with a quadrangular base, for a " +
                    "height of about 15 meters. The pylons support lowered arches (four remain) with a radius of 3.35 m, " +
                    "at the top of which is the conduit intended for the flow of water. The aqueduct route extends for a total " +
                    "length of about 12 km, starting from the water collection basin located in Lagoscuro (municipality of Cartosio).";
        }else if(codec.equals("QDMV01-AL")){
            return "The castle of the paleologists dates back, in its original layout, at least to the 11th century: the first" +
                    " written source that certifies its existence with certainty - a source in which it is remembered as a castelletto" +
                    " - dates back to 1056. It was, at first, residence of the bishops-counts of Acqui; it later became a stronghold " +
                    "of the medieval village of Acqui and the seat of the governors of the city and then, starting from 1260," +
                    "it passed to the Marquises Paleologi del Monferrato. Starting from 1708, however, the complex passed under" +
                    "the direct administration of the House of Savoy which progressively deprived it of its original function " +
                    "as a fortress, assigning it to that function of prison which it maintained until recent years.";
        }else if(codec.equals("MELC02-AL")){
            return "The history of Villa Ottolenghi begins in 1923, when two young spouses, Arturo B. Ottolenghi and Herta Von Wedekind, " +
                    "acquired an ancient residence on the hills of Borgo Monterosso, so called since the Middle " +
                    "Ages for the vegetation made up of trees with reddish foliage. In love with the panorama that can " +
                    "still be admired today from any point of the villa, they entrusted the design first to Federico d’Amato " +
                    "and then to the famous architect Marcello Piacentini. On the death of Arturo first, then of Herta," +
                    "it is the son Astolfo who undertakes to finish the work of his parents. Not far from the villa there " +
                    "is the Mausoleum, today the Temple of Herta, and the park obtained from a precise scenographic design " +
                    "that takes the name of Earthly Paradise";
        }else{
            return "Disco-Quiz";
        }
    }

}
