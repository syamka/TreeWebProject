package tree.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Title:
 * Description:
 * <p/>
 * User: valentina
 * Date: 21.01.14
 * Time: 23:13
 */
@XmlRootElement
public class OperationResponse {

    public OperationResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean status;
    public String message;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
