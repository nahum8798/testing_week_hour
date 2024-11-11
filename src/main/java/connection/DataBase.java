package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    private static DataBase instance;
    private Connection connection = null;
    private ResultDataBase resultDataBase = null;

    //Declaramos las variables para conectarnos a la base de datos
    //al declarar static final antes de la variable hacemos que no pueda cambiarse nunca
    private static final String USER = "ies9021_userdb";
    private static final String PASSWORD = "Xsw23edc.127";
    private static final String DATABASE = "ies9021_coco";
    private static final String HOST = "ies9021.edu.ar";
    private static final String PORT = "3306";
    private static final String CONNECTION_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

    //establecemos la conexión con la base de datos
    private DataBase(){
    }

    //Método para que solo se inicialice una vez para no ocupar espacio en memoria, si ya hay una clase DataBase instanciada esta misma se vuelve a llamar
    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    //Abrir conexión
    private Connection openConnection(){
        try {
            connection = DriverManager.getConnection(CONNECTION_URL,USER,PASSWORD);
            System.out.println("Conexión abierta correctamente");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    //Cerrar conexión
    private void closeConnection(){
        if(connection != null){
            try{
                connection.close();
                System.out.println("Conexion cerrada correctamente");
            } catch(SQLException e){
                System.err.println("Error al cerrar la conexion" +e.getMessage());
            }
        }
    }

    public ResultDataBase getResultDataBase() {
        return resultDataBase;
    }

    public void setResultDataBase(ResultDataBase resultDataBase) {
        this.resultDataBase = resultDataBase;
    }

    //Método para conocer la cantidad de PlaceHolders que contiene la query
    private int countPlaceholders(String query) {
        // Contar los caracteres '?' en la consulta
        int cant = 0;
        if (query != null) {
            //toCharArray() -> metodo que convierte toda la cadena de texto a caracteres individuales
            for (char c : query.toCharArray()) {
                if (c == '?') {
                    cant++; //si el caracter c coincide con "?" incrementamos la variable
                }
            }
        }
        return cant;
    }

    public ResultDataBase create(String query, Object... params) {

        // Inicializamos la variable para almacenar el resultado
        ResultDataBase resultdb = new ResultDataBase();
        // Variable que va a almacenar la cantidad de PlaceHolders que tiene la consulta
        int numPlaceholders = countPlaceholders(query);

        // Verificar si el número de parámetros coincide con el número de placeHolders
        if (params.length != numPlaceholders) {
            resultdb.setSuccess(false);
            resultdb.setMessage("El número de parámetros no coincide con el número de marcadores(?) en la consulta. Consulta: " + query +
                    ". Parámetros proporcionados: " + Arrays.toString(params) +
                    ". Número de marcadores: " + numPlaceholders);
            return resultdb;
        }

        // Verificamos si la consulta es nula
        if (query == null) {
            resultdb.setSuccess(false);
            resultdb.setMessage("La consulta está vacía");
            return resultdb;
        }

        //Validamos que la consulta comience con un INSERT
        if(!query.trim().toUpperCase().startsWith("INSERT")){
            resultdb.setSuccess(false);
            resultdb.setMessage("La consulta ingresada no contiene un insert. La consulta fue: "+query);
            return resultdb;
        }
        // Esta variable se va a ocupar para preparar y ejecutar la consulta SQL
        PreparedStatement statement = null;

        try {
            //abrimos la conexion
            openConnection();
            // Preparamos la consulta con la opción de recuperar las claves generadas
            // PreparedStatement.RETURN_GENERATED_KEYS -> indica que queremos las claves generadas automáticamente
            statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            // Asignamos los parámetros a la consulta
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]); // Los índices en SQL comienzan en 1, no en 0
            }

            // Ejecutamos la consulta de actualización y obtenemos el número de filas afectadas
            int filasAfectadas = statement.executeUpdate();
            if(filasAfectadas == 0){
                resultdb.setSuccess(false);
                resultdb.setMessage("Error al crear. Verifica que los datos de la consulta estén bien ingresados: "+query);
            }else{
                // Buscamos el id que se genero de manera automática en la base de datos
                ResultSet idGenerado = statement.getGeneratedKeys();
                // Si hay un ID ejecutamos este bloque
                if (idGenerado.next()) {
                    // Variable que almacena el idgenerado automaticamente
                    Object generatedId = idGenerado.getObject(1);
                    resultdb.setSuccess(true);
                    resultdb.setMessage("La consulta se realizó con éxito. El ID asignado es:");
                    resultdb.setObject(generatedId); // Almacena el ID generado en el resultado
                } else {
                    resultdb.setSuccess(true);
                    resultdb.setMessage("La consulta se realizó con éxito, pero no se generó ninguna clave.");
                }
            }

        } catch (SQLException e) {
            resultdb.setSuccess(false);
            String errorMessage = "Error SQL " +e.getSQLState()+ ":"+e.getMessage() +". Consulta: "+query +". Parámetros: "+Arrays.toString(params);
            resultdb.setMessage(errorMessage);
        } finally {
            //Cerramos PreparedStatement para liberar recursos
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {

                }
            }
            // Finalmente, cerramos la conexión a la base de datos
            if (connection != null) {
                closeConnection();
            }
        }
        // Retornamos el objeto ResultDataBase con el resultado de la operación
        return resultdb;
    }

    public ResultDataBase select(String query, Object...params){
        //variable que va a almacenar el resultado que retornemos
        ResultDataBase resultdb = new ResultDataBase();
        //variable que almacena la cantidad de placeHolders que tiene la consulta
        int numPlaceholders = countPlaceholders(query);
        // Verificar que el número de parámetros coincide con el número de marcadores
        if (params.length != numPlaceholders) {
            resultdb.setSuccess(false);
            resultdb.setMessage("El número de parámetros no coincide con el número de marcadores(?) en la consulta. Consulta: " + query +
                    ". Parámetros proporcionados: " + Arrays.toString(params) +
                    ". Número de marcadores: " + numPlaceholders);
            return resultdb;
        }
        //verificamos si la consulta no es nula
        if(query == null){
            resultdb.setSuccess(false);
            resultdb.setMessage("La consulta esta vacia");
            return resultdb;
        }
        //Validamos que la consulta comience con SELECT
        if(!query.trim().toUpperCase().startsWith("SELECT")){
            resultdb.setSuccess(false);
            resultdb.setMessage("La consulta realizada no contiene un select. La consulta fue: "+query);
        }
        //preparamos la consulta
        PreparedStatement statement = null;
        ResultSet rs = null;
        try{
            //abrimos la conexion
            openConnection();
            statement = connection.prepareStatement(query);
            //seteamos los parametros
            for(int i = 0 ; i<params.length ; i++){
                statement.setObject(i + 1, params[i]);
            }
            //Ejecutamos la consulta y la almacenamos en un ResultSet(nos va a dar los datos de cada fila)
            rs = statement.executeQuery();
            //Creamos una lista para almacenar todas las filas de datos recuperadas por ResultSet (es la lista que vamos a retornar)
            List<Object> resultado = new ArrayList<>();
            //Iteramos sobre cada fila del ResultSet (siempre y cuando existan filas para iterar)
            while(rs.next()){
                //Obtenemos la cantidad de columnas para saber cuantas veces iterar y obtener(get) los datos
                int cantColum = rs.getMetaData().getColumnCount();
                //Almacenamos los datos que contiene el rs, al ser de tipo Object almacena cualquier tipo de dato (String, int, boolean, etc)
                List<Object> datos = new ArrayList<>();
                //Recorre cada cada columna de la fila actual
                for(int i = 1 ; i <= cantColum ; i++){
                    //agrega el valor de cada columna a la lista datos, lo hace mediante un getObject() por lo que no nos preocuparía que tipo de dato es
                    datos.add(rs.getObject(i));
                }
                //agrega la lista datos a resultado para luego retornarlo
                resultado.add(datos);
            }
            // Verificamos si la lista de resultados está vacía
            if (resultado.isEmpty()) { //Este metodo nos devuelve TRUE si la lista esta vacia
                resultdb.setSuccess(false);
                resultdb.setMessage("No se encontraron resultados para la consulta. La consulta fue: " +query +" y los parámetros fueron: "+Arrays.toString(params));
            } else {
                resultdb.setSuccess(true);
                resultdb.setMessage("La consulta se realizó con éxito");
                resultdb.setObject(resultado);
            }
        } catch(SQLException e){
            //si hay un error retornamos un false e indicamos que error fue
            resultdb.setSuccess(false);
            String errorMessage = "Error SQL " +e.getSQLState()+ ":"+e.getMessage() +". Consulta: "+query +". Parámetros: "+Arrays.toString(params);
            resultdb.setMessage(errorMessage);
        } finally{
            //cerramos ResultSet
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException ex) {}
            }
            //cerramos PreparedStatement
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException ex) {}
            }
            //cerramos la conexión
            if(connection != null){
                closeConnection();
            }
        }
        return resultdb;
    }

    public ResultDataBase update(String query, Object...params){
        //variable que va a almacenar el resultado que retornemos
        ResultDataBase resultdb = new ResultDataBase();
        //variable que almacena la cantidad de placeHolders que tiene la consulta
        int numPlaceholders = countPlaceholders(query);
        // Verificar que el número de parámetros coincide con el número de marcadores
        if (params.length != numPlaceholders) {
            resultdb.setSuccess(false);
            resultdb.setMessage("Número de parámetros no coincide con el número de marcadores(?) en la consulta. Consulta: " + query +
                    ". Parámetros proporcionados: " + Arrays.toString(params) +
                    ". Número de marcadores: " + numPlaceholders);
            return resultdb;
        }
        //verificamos que la consulta no llegue nula
        if(query == null){
            resultdb.setSuccess(false);
            resultdb.setMessage("La consulta esta vacia");
            return resultdb;
        }
        //Verificamos que la consulta venga con un WHERE
        //query.trim()-> Elimina los espacios en blanco. toUpperCase()-> Convierte todo en mayusculas. startsWith()-> Nos dice con que comienza la cadena
        //contains() -> Nos dice si la cadena contiene lo que le pasemos por parámetro
        if(query.trim().toUpperCase().startsWith("UPDATE") && !query.toUpperCase().contains("WHERE")) {
            resultdb.setSuccess(false);
            resultdb.setMessage("ERROR NO PUEDE ENVIAR UN UPDATE SIN UN WHERE. La consulta realizada fue: "+query);
            return resultdb;
        }
        if(!query.trim().toUpperCase().contains("UPDATE")){
            resultdb.setSuccess(false);
            resultdb.setMessage("La consulta ingresa no contiene un update. La consulta ingresada fue: "+query);
            return resultdb;
        }
        //preparamos para la consulta SQL
        PreparedStatement statement = null;
        try{
            openConnection();
            statement = connection.prepareStatement(query);
            //recorremos el objeto llegado con los parametros y los asignamos
            for(int i = 0 ; i<params.length ; i++){
                statement.setObject(i + 1, params[i]);
            }
            //ejecutamos la consulta para saber la cantidad de filas que fueron afectadas
            int filasAfectadas = statement.executeUpdate();
            if(filasAfectadas == 0){
                resultdb.setSuccess(false);
                resultdb.setMessage("Error la columna que quiere actualizar no existe");
            }else{
                resultdb.setSuccess(true);
                resultdb.setMessage("Se actualizó correctamente");
            }
        }catch(SQLException e){
            resultdb.setSuccess(false);
            String errorMessage = "Error SQL " +e.getSQLState()+ ":"+e.getMessage() +". Consulta: "+query +". Parámetros: "+Arrays.toString(params);
            resultdb.setMessage(errorMessage);
        }finally{
            //Cerramos PreparedStatement
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException ex) {}
            }
            //Cerramos la conexion
            if(connection != null){
                closeConnection();
            }
        }
        return resultdb;
    }

    public ResultDataBase delete(String query, Object...params){
        //Variable que va a ser retornada
        ResultDataBase resultdb = new ResultDataBase();
        //variable que almacena la cantidad de placeHolders que tiene la consulta
        int numPlaceholders = countPlaceholders(query);
        // Verificar que el número de parámetros coincide con el número de marcadores
        if (params.length != numPlaceholders) {
            resultdb.setSuccess(false);
            resultdb.setMessage("Número de parámetros no coincide con el número de marcadores(?) en la consulta. Consulta: " + query +
                    ". Parámetros proporcionados: " + Arrays.toString(params) +
                    ". Número de marcadores: " + numPlaceholders);
            return resultdb;
        }
        //preparamos para la sentenecia SQL
        PreparedStatement statement = null;
        //Verificamos que la consulta no este vacia
        if(query == null){
            resultdb.setSuccess(false);
            resultdb.setMessage("No puede enviar una consulta vacía");
            return resultdb;
        }
        //Verificamos que la consulta contenga un WHERE
        if((query.trim().toUpperCase().startsWith("DELETE") || query.trim().toUpperCase().startsWith("TRUNCATE")) &&
                !query.trim().toUpperCase().contains("WHERE")){
            resultdb.setSuccess(false);
            resultdb.setMessage("ERROR NO PUEDE ENVIAR UN DELETE O UN TRUNCATE SIN UN WHERE. La consulta realizada fue: "+query);
            return resultdb;
        }
        //Verificamos si la consulta contiene un DELETE ó TRUNCATE
        if(!query.trim().toUpperCase().contains("DELETE") || !query.trim().toUpperCase().contains("TRUNCATE")){
            resultdb.setSuccess(false);
            resultdb.setMessage("No se puede realizar una eliminación sin un delete o un truncate. La consulta fue: "+query);
        }

        try{
            //abrimos la conexion
            openConnection();
            statement = connection.prepareStatement(query);
            //recorremos para asignar los parametros
            for(int i = 0; i<params.length; i++){
                statement.setObject(1+i, params[i]);
            }
            //ejecutamos la consulta y almacenamos la cantidad de columnas afectadas
            int filasAfectadas = statement.executeUpdate();
            if(filasAfectadas == 0){
                resultdb.setSuccess(false);
                resultdb.setMessage("Error la columna que quiere eliminar no existe");
            }else{
                resultdb.setSuccess(true);
                resultdb.setMessage("Se eliminó correctamente");
            }

        } catch(SQLException e){
            resultdb.setSuccess(false);
            String errorMessage = "Error SQL " +e.getSQLState()+ ":"+e.getMessage() +". Consulta: "+query +". Parámetros: "+Arrays.toString(params);
            resultdb.setMessage(errorMessage);
        }finally{
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException ex) {}
            }
            if(connection != null){
                closeConnection();
            }
        }
        return resultdb;
    }
}
