package tree.rest;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Title:
 * Description:
 * <p/>
 * User: valentina
 * Date: 22.01.14
 * Time: 0:17
 */
@XmlRootElement
public class SearchResult {

    public Set<BigInteger> openBranches = new LinkedHashSet<BigInteger>();
    public Set<BigInteger> results = new LinkedHashSet<BigInteger>();

    public void addBranch(BigInteger id){
        openBranches.add(id);
    }

    public void addBranches(List<BigInteger> ids){
        openBranches.addAll(ids);
    }

    public void addResult(BigInteger id){
        results.add(id);
    }

}
