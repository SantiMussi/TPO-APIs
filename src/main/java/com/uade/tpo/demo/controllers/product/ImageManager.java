package com.uade.tpo.demo.controllers.product;


import java.io.*;
import java.util.Arrays;
import java.util.Base64;

public class ImageManager {


    /*


    METODOS PARA CONVERSION DE IMAGEN A STRING Y VICEVERSA

    Pipeline foto

    Front ----> Encode en front ----> Json ----> Decode y save en back, Se almacena ubi en la base ----> Encode en back ----> JSON----> Decode en front
    |--------------------------------------- POST -----------------------------------------------------| |----------------- GET ----------------------|

     */



    public static void main(String[] args){
        //File file = base64save("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KPHN2ZyBpZD0ic3ZnMiIgd2lkdGg9IjYyMCIgaGVpZ2h0PSI0NzIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPgogPGRlZnMgaWQ9ImRlZnM0Ij4KICA8cGF0aCBpZD0iYm94MSIgZD0ibTAgMGg3N3YyMTBoLTc3eiIgc3Ryb2tlPSIjMDAwIiBzdHJva2Utd2lkdGg9IjIiLz4KICA8cGF0aCBpZD0iYm94MiIgZD0ibTAgMGg3N3Y2MGgtNzd6IiBzdHJva2U9IiMwMDAiIHN0cm9rZS13aWR0aD0iMiIvPgogPC9kZWZzPgogPHBhdGggaWQ9ImJnIiBkPSJtMCAwaDYyMHY0NzJoLTYyMHoiIGZpbGw9IiNmZmYiLz4KPGcgZm9udC1mYW1pbHk9IkRlamFWdSBTYW5zLCBBcmlhbCwgSGVsdmV0aWNhIiBzdHJva2Utd2lkdGg9IjEiIHhtbDpzcGFjZT0icHJlc2VydmUiPgogPGcgaWQ9Imc5IiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgyIDEpIj4KICA8dXNlIGlkPSJ1c2UxMSIgZmlsbD0iI2ZmZiIgeGxpbms6aHJlZj0iI2JveDEiLz4KICA8dXNlIGlkPSJ1c2UxMyIgeD0iNzciIGZpbGw9IiNmZjAiIHhsaW5rOmhyZWY9IiNib3gxIi8+CiAgPHVzZSBpZD0idXNlMTUiIHg9IjE1NCIgZmlsbD0iIzBmZiIgeGxpbms6aHJlZj0iI2JveDEiLz4KICA8dXNlIGlkPSJ1c2UxNyIgeD0iMjMxIiBmaWxsPSIjMGYwIiB4bGluazpocmVmPSIjYm94MSIvPgogIDx1c2UgaWQ9InVzZTE5IiB4PSIzMDgiIGZpbGw9IiNmMGYiIHhsaW5rOmhyZWY9IiNib3gxIi8+CiAgPHVzZSBpZD0idXNlMjEiIHg9IjM4NSIgZmlsbD0icmVkIiB4bGluazpocmVmPSIjYm94MSIvPgogIDx1c2UgaWQ9InVzZTIzIiB4PSI0NjIiIGZpbGw9IiMwMGYiIHhsaW5rOmhyZWY9IiNib3gxIi8+CiAgPHVzZSBpZD0idXNlMjUiIHg9IjUzOSIgeGxpbms6aHJlZj0iI2JveDEiLz4KIDwvZz4KIDxnIGlkPSJnNDUiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDIgMjIwKSI+CiAgPHVzZSBpZD0idXNlNDciIGZpbGw9IiMwZjAiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlNDkiIHg9Ijc3IiBmaWxsPSIjMGYwIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx1c2UgaWQ9InVzZTUxIiB4PSIxNTQiIGZpbGw9IiMwZjAiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlNTMiIHg9IjIzMSIgZmlsbD0iIzBmMCIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJ1c2U1NSIgeD0iMzA4IiBmaWxsPSIjZmZmIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx1c2UgaWQ9InVzZTU3IiB4PSIzODUiIGZpbGw9IiNmZmYiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlNTkiIHg9IjQ2MiIgZmlsbD0iI2ZmZiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJ1c2U2MSIgeD0iNTM5IiBmaWxsPSIjZmZmIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx0ZXh0IGlkPSJncmVlbjEwMCIgeD0iMzAiIHk9IjM1IiBmaWxsPSIjZmZmIj4wLjU5PC90ZXh0PgogPC9nPgogPGcgaWQ9ImcyNyIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMiAyODApIj4KICA8dXNlIGlkPSJ1c2UyOSIgZmlsbD0icmVkIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx1c2UgaWQ9InVzZTMxIiB4PSI3NyIgZmlsbD0icmVkIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx1c2UgaWQ9InVzZTMzIiB4PSIxNTQiIGZpbGw9IiNmZmYiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlMzUiIHg9IjIzMSIgZmlsbD0iI2ZmZiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJ1c2UzNyIgeD0iMzA4IiBmaWxsPSJyZWQiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlMzkiIHg9IjM4NSIgZmlsbD0icmVkIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx1c2UgaWQ9InVzZTQxIiB4PSI0NjIiIGZpbGw9IiNmZmYiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlNDMiIHg9IjUzOSIgZmlsbD0iI2ZmZiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dGV4dCBpZD0icmVkMTAwIiB4PSIyMCIgeT0iMzUiIGZpbGw9IiNmZmYiPiswLjMwPC90ZXh0PgogPC9nPgogPGcgaWQ9Imc2MyIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMiAzNDApIj4KICA8dXNlIGlkPSJ1c2U2NSIgZmlsbD0iIzAwZiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJ1c2U2NyIgeD0iNzciIGZpbGw9IiNmZmYiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlNjkiIHg9IjE1NCIgZmlsbD0iIzAwZiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJ1c2U3MSIgeD0iMjMxIiBmaWxsPSIjZmZmIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx1c2UgaWQ9InVzZTczIiB4PSIzMDgiIGZpbGw9IiMwMGYiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0idXNlNzUiIHg9IjM4NSIgZmlsbD0iI2ZmZiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJ1c2U3NyIgeD0iNDYyIiBmaWxsPSIjMDBmIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx1c2UgaWQ9InVzZTc5IiB4PSI1MzkiIGZpbGw9IiNmZmYiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHRleHQgaWQ9ImJsdWUxMDAiIHg9IjIwIiB5PSIzNSIgZmlsbD0iI2ZmZiI+KzAuMTE8L3RleHQ+CiA8L2c+CiA8ZyBpZD0iZzYzIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgyIDQxMCkiPgogIDx1c2UgaWQ9ImdyZXkxMDAiIGZpbGw9IiNmZmYiIHhsaW5rOmhyZWY9IiNib3gyIi8+CiAgPHVzZSBpZD0iZ3JleTg5IiB4PSI3NyIgZmlsbD0iI2UzZTNlMyIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJncmV5NzAiIHg9IjE1NCIgZmlsbD0iI2IyYjJiMiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJncmV5NTkiIHg9IjIzMSIgZmlsbD0iIzk2OTY5NiIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJncmV5NDEiIHg9IjMwOCIgZmlsbD0iIzY5Njk2OSIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJncmV5MzAiIHg9IjM4NSIgZmlsbD0iIzRkNGQ0ZCIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJncmV5MTEiIHg9IjQ2MiIgZmlsbD0iIzFjMWMxYyIgeGxpbms6aHJlZj0iI2JveDIiLz4KICA8dXNlIGlkPSJncmV5MCIgeD0iNTM5IiBmaWxsPSIjMDAwIiB4bGluazpocmVmPSIjYm94MiIvPgogIDx0ZXh0IGlkPSJ0eGdyZXkxMDAiIHg9IjIwIiB5PSIzNSI+MTAwJTwvdGV4dD4KICA8dGV4dCBpZD0idHhncmV5ODkiIHg9IjEwMiIgeT0iMzUiPjg5JTwvdGV4dD4KICA8dGV4dCBpZD0idHhncmV5NzAiIHg9IjE3OSIgeT0iMzUiPjcwJTwvdGV4dD4KICA8dGV4dCBpZD0idHhncmV5NTkiIHg9IjI1NiIgeT0iMzUiPjU5JTwvdGV4dD4KICA8dGV4dCBpZD0idHhncmV5NDEiIHg9IjMzMyIgeT0iMzUiIGZpbGw9IiNmZmYiPjQxJTwvdGV4dD4KICA8dGV4dCBpZD0idHhncmV5MzAiIHg9IjQwOCIgeT0iMzUiIGZpbGw9IiNmZmYiPjMwJTwvdGV4dD4KICA8dGV4dCBpZD0idHhncmV5MTEiIHg9IjQ4NyIgeT0iMzUiIGZpbGw9IiNmZmYiPjExJTwvdGV4dD4KICA8dGV4dCBpZD0idHhncmV5MCIgeD0iNTY5IiB5PSIzNSIgZmlsbD0iI2ZmZiI+MCU8L3RleHQ+CiA8L2c+CiA8dGV4dCBpZD0idGV4dDM0NDYtMCIgeD0iOTAiIHk9IjE4NCIgZmlsbD0iI2ZmZiIgZm9udC1zaXplPSIxODAiIHN0cm9rZS13aWR0aD0iNCI+VEVTVDwvdGV4dD4KIDx0ZXh0IGlkPSJ0ZXh0MzQ0NiIgeD0iODAiIHk9IjE3NCIgc3Ryb2tlLXdpZHRoPSI0Ij48dHNwYW4gaWQ9InRzcGFuMzQ0OCIgeD0iODAiIHk9IjE3NCIgZm9udC1zaXplPSIxODAiPlRFU1Q8L3RzcGFuPjwvdGV4dD4KPC9nPgo8L3N2Zz4=","");
        //fileToBase64(file);
    }


    public static byte[] base64tobyteArray(String base64, String dir){

        String[] data = base64.split(",");
        System.out.println(Arrays.toString(data));

        return Base64.getDecoder().decode(data[1]);

        /*try {
            File wfile = new File(dir + ".png");
            wfile.getParentFile().mkdirs();
            if (wfile.createNewFile()){
                System.out.println("File Created!");
                FileOutputStream file = new FileOutputStream(wfile);
                file.write(bytedata);
            } else {
                System.out.println("File not created");
            }


            return wfile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    public static String fileToBase64(byte[] data){

        String dataString = Base64.getEncoder().encodeToString(data);
        String finalString = "data:image/png;base64, " + dataString;
        return finalString;

        /*try {
            String dataString = Base64.getEncoder().encodeToString((new FileInputStream(file)).readAllBytes());
            String filext = file.getName().split("\\.")[1];
            String finalString = "data:image/"+ filext +";base64, " + dataString;
            System.out.println(finalString);

            return finalString;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        */
        }


}
