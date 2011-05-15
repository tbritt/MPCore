#if ($entity.packageName)
package $entity.packageName;

#end
import org.apache.log4j.Logger;

public class ${entity.classNameWithoutPackage} extends ${entity.prefixClassNameWithOptionalPackage} {
    
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(${entity.classNameWithoutPackage}.class);
    
}
