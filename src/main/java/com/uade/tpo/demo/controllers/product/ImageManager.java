package com.uade.tpo.demo.controllers.product;


import java.util.Base64;

public class ImageManager {


    /*


    METODOS PARA CONVERSION DE IMAGEN A STRING Y VICEVERSA

    Pipeline foto

    Front ----> Encode en front ----> Json ----> Decode y save en back, Se almacena como blob ----> Encode en back ----> JSON----> Decode en front
    |--------------------------------------- POST ------------------------------------------------| |----------------- GET ----------------------|

     */



    public static void main(String[] args){
        // FUNCION PARA TESTING ----- IGNORAR
    }


    public static byte[] base64tobyteArray(String base64){

        String[] data = base64.split(",");
        //System.out.println(Arrays.toString(data));

        return Base64.getDecoder().decode(data[1]);

    }

    public static String fileToBase64(byte[] data){

        String dataString = Base64.getEncoder().encodeToString(data);
        return "data:image/png;base64, " + dataString;

        }


}
