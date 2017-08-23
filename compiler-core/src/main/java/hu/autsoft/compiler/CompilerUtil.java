package hu.autsoft.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompilerUtil {

    private CompilerUtil() {
        throw new IllegalAccessError("Utility class");
    }

    public static String getJavaString(String packageName, TypeSpec typeSpec) {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).skipJavaLangImports(true).build();
        return javaFile.toString();
    }

    public static boolean isPackageUnnamed(Element objectClass) {
        PackageElement packageElement = (PackageElement) (objectClass.getEnclosingElement());
        return packageElement.isUnnamed();
    }

    public static String getPackageNameForClass(Element classElement) {
        return getPackageNameForClass(classElement, null);
    }

    public static String getPackageNameForClass(Element classElement, String postPackageNames) {
        boolean hasPostPackageNames = postPackageNames != null;
        PackageElement packageElement = (PackageElement) (classElement.getEnclosingElement());
        return packageElement.toString() + (hasPostPackageNames ? ("." + postPackageNames) : "");
    }

    public static void writeJavaFile(ProcessingEnvironment processingEnvironment, TypeHelper classTypeHelper, TypeSpec typeSpec) throws IOException {
        String filename = classTypeHelper.getStrFullClassName();
        String content = CompilerUtil.getJavaString(classTypeHelper.getStrPackageName(), typeSpec);
        JavaFileObject jfo = processingEnvironment.getFiler().createSourceFile(filename);
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "creating source file: " + jfo.toUri());
        Writer writer = jfo.openWriter();
        writer.write(content);
        writer.close();
    }

}
