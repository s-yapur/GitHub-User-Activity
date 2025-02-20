import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ghucli{
    public static void main(String[] args) {

        if (args.length != 1) { // Verifica si el usuario ingreso una entrada valida
            System.out.println("Uso: java ghucli <nombre_de_usuario>");
            System.exit(1);
        }

        String user = args[0];
        String ghApi = "https://api.github.com/users/" + user + "/events/public";

        try { // Crear la URL y la conexión HTTP
            URL url = new URL(ghApi); // Transforma el string ghApi en un objeto tipo URL para poder realizar una conexion HTTP
            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // Inicia una coneccion HTTP con la API
            
            http.setRequestMethod("GET"); // El comando "GET" sirve para solicitar datos a traves de la conexion HTTP
            http.setRequestProperty("User-Agent", "Mozilla/5.0"); // El user-agent es necesario para que la API pueda reconocer que la solicitud es enviada desde una fuente legetima, a parte enviar los datos que correspondan con la fuente

            int codeResp = http.getResponseCode();// Verificar si la solicitud fue respuesta correctamente, si es asi, el codigo de respuesta debe ser '200'
            if (codeResp != 200) {
                System.out.println("Error: No se obtuvo la respuesta deseada, codigo de respuesta recibido:  " + codeResp);
                return;
            }

            // Leer la respuesta de la API
            BufferedReader inn = new BufferedReader(new InputStreamReader(http.getInputStream())); // Permite obtener los datos del flujo de entrada de la conexion http para leer, linea por linea, los datos recibidos mediante la misma
            String gotcha; // Copiara linea por linea lo que recibamos de la API 
            StringBuffer response = new StringBuffer(); // Almacenara todas las lineas de respuestas
            while ((gotcha = inn.readLine()) != null) { // Copia una linea de respuesta en la cadena de texto
                response.append(gotcha); // Luego es almacenada
            } // Termina cuando la linea recibida sea nula
            inn.close(); // Cierra el flujo de entrada

            // Mostrar la actividad
            String activity = response.toString(); // Convierte la lista de lineas de textos almacenadas en un solo texto
            if (activity.contains("message")) { // Este texto indicaria que el usuario no existe o hubo algun problema con la API
                System.out.println("Usuario no encontrado o error en la API.");
            } else {
                System.out.println("Actividad reciente de " + user + ":"); // Buchonea quien rompio el programa
                System.out.println(activity); // Muestra linea por linea la respuesta entera de la API
            }

        } catch (Exception e) { // Si hubo algun error en el intento de conexion con la API o durante la obtencion de datos, se mostrara un mensaje de error
            System.out.println("Error al hacer la solicitud: " + e.getMessage());
            e.printStackTrace(); // Muestra detalles mas a fondo de la excepcion
        }
    }
}

    