package connection;

/*
   Clase que representa el resultado de una operación en la base de datos.
   Esta clase contiene la información sobre el éxito de la operación, un mensaje relacionado y un objeto de datos que se puede incluir en el resultado.
 */
public class ResultDataBase {

    /**
     * Indica si la operación fue exitosa o no.
     */
    private Boolean success;

    /**
     * Mensaje que proporciona información adicional sobre el resultado de la operación.
     */
    private String message;

    /**
     * Objeto que contiene los datos resultantes de la operación. Puede ser cualquier tipo de objeto.
     */
    private Object object;

    /* Constructor por defecto. */
    public ResultDataBase() {
    }

    /* Constructor que inicializa todos los campos de la clase.*/
    public ResultDataBase(Boolean success, String message, Object object) {
        this.success = success;
        this.message = message;
        this.object = object;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}

