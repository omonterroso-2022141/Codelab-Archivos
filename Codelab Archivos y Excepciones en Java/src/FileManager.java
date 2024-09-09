import java.io.*;
import java.util.Scanner;

// Dentro de la clase FileManager

// Excepción personalizada para manejar archivos no encontrados
class ArchivoNoEncontradoException extends Exception {
    public ArchivoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

// Excepción personalizada para manejar la creación de un archivo que ya existe
class ArchivoYaExisteException extends Exception {
    public ArchivoYaExisteException(String mensaje) {
        super(mensaje);
    }
}

public class FileManager {

    // Método para verificar la existencia de un archivo
    public static void verificarArchivo(String nombreArchivo) throws ArchivoNoEncontradoException {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            throw new ArchivoNoEncontradoException("El archivo no existe.");
        }
    }

    // Método para crear un archivo
    public static void crearArchivo(String nombreArchivo) throws ArchivoYaExisteException {
        File archivo = new File(nombreArchivo);
        if (archivo.exists()) {
            throw new ArchivoYaExisteException("El archivo ya existe.");
        } else {
            try {
                archivo.createNewFile();
                System.out.println("Archivo " + nombreArchivo + " creado exitosamente.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para agregar una nueva línea de texto a un archivo existente
    public static void agregarLinea(String nombreArchivo, String texto) throws IOException {
        try (FileWriter writer = new FileWriter(nombreArchivo, true)) { // true para modo append
            writer.write(texto + "\n");
            System.out.println("Texto agregado al archivo.");
        }
    }

    // Método para leer y mostrar el contenido completo de un archivo
    public static void leerArchivo(String nombreArchivo) throws IOException, ArchivoNoEncontradoException {
        verificarArchivo(nombreArchivo); // Asegurarse de que el archivo existe antes de leerlo
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            System.out.println("Contenido del archivo:");
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }
    }

    // Método para leer y mostrar una línea específica de un archivo
    public static void leerLineaEspecifica(String nombreArchivo, int numeroLinea) throws IOException, ArchivoNoEncontradoException {
        verificarArchivo(nombreArchivo); // Asegurarse de que el archivo existe antes de leerlo
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            int contador = 1;
            while ((linea = reader.readLine()) != null) {
                if (contador == numeroLinea) {
                    System.out.println("Línea " + numeroLinea + ": " + linea);
                    return;
                }
                contador++;
            }
            System.out.println("No se encontró la línea " + numeroLinea);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo:");

        String nombreArchivo = scanner.nextLine();

        // Utiliza un bloque try-catch para llamar a verificarArchivo y crearArchivo.
        try {
            verificarArchivo(nombreArchivo);
        } catch (ArchivoNoEncontradoException e) {
            System.out.println(e.getMessage());
            System.out.println("¿Desea crear el archivo? (sí/no):");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("sí")) {
                try {
                    crearArchivo(nombreArchivo);
                } catch (ArchivoYaExisteException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                System.out.println("Operación cancelada.");
                scanner.close();
                return;
            }
        }

        // Implementa una interfaz de usuario en la consola sencilla que permita al usuario elegir entre:
        // 1. Agregar nuevas líneas al archivo.
        // 2. Mostrar el contenido del archivo.
        // 3. Mostrar una línea específica del archivo.
        boolean continuar = true;

        while (continuar) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Agregar nuevas líneas al archivo");
            System.out.println("2. Mostrar el contenido del archivo");
            System.out.println("3. Mostrar una línea específica del archivo");
            System.out.println("4. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.println("Escriba el texto que desea agregar:");
                    String texto = scanner.nextLine();
                    try {
                        agregarLinea(nombreArchivo, texto);
                    } catch (IOException e) {
                        System.out.println("Error al agregar texto: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        leerArchivo(nombreArchivo);
                    } catch (IOException | ArchivoNoEncontradoException e) {
                        System.out.println("Error al leer el archivo: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Ingrese el número de línea que desea leer:");
                    int numeroLinea = scanner.nextInt();
                    try {
                        leerLineaEspecifica(nombreArchivo, numeroLinea);
                    } catch (IOException | ArchivoNoEncontradoException e) {
                        System.out.println("Error al leer la línea específica: " + e.getMessage());
                    }
                    break;
                case 4:
                    continuar = false;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida, intente de nuevo.");
            }
        }
        scanner.close();
    }


}
