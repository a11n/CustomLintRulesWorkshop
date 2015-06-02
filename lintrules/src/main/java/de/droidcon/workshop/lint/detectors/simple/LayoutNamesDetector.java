package de.droidcon.workshop.lint.detectors.simple;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import java.util.Arrays;
import java.util.List;
import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;

/**
 * Checks if layouts referenced by a certain Activity/Fragment
 * have a corresponding name.
 *
 * Pattern: [activity|fragment]_class_name
 *
 * Examples:
 *
 * activity_order_status.xml
 * fragment_order_details.xml
 *
 * References:
 * -----------
 * setContentView(R.layout.activity_order_status); //in ActivityOrderStatus.java
 */
public class LayoutNamesDetector extends Detector implements Detector.JavaScanner {

  public static final Issue ISSUE =
      Issue.create("LayoutReference", "Invalid layout reference",
          "Layouts referenced by Activities or Fragments must be named with pattern "
              + "[activity|fragment]_class_name while 'class_name' corresponds to "
              + "the class name of the Activity or Fragment.\n"
              + "Example: activity_order_status", Category.CORRECTNESS, 5, Severity.WARNING,
          new Implementation(LayoutNamesDetector.class, Scope.JAVA_FILE_SCOPE));

  @Override public List<String> getApplicableMethodNames() {
    return Arrays.asList("setContentView", "inflate");
  }

  @Override
  public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
    String methodName = node.astName().astValue();

    if ("setContentView".equals(methodName)) {
      visitSetContentView(context, node);
    } else if ("inflate".equals(methodName)) visitInflate(context, node);
  }

  private void visitSetContentView(JavaContext context, MethodInvocation node) {
    if (node.astArguments().size() != 1) return;

    String argument = node.astArguments().first().toString();
    String layoutName = argument.substring(9); //strip 'R.layout.'

    String className = LintUtils.getBaseName(context.file.getName());
    String expectedLayoutName = camelToUnderscore(className);

    if (!expectedLayoutName.equals(layoutName)) {
      context.report(ISSUE, context.getLocation(node),
          String.format("Class '%s' should reference '%s' instead of '%s'.", className,
              expectedLayoutName, layoutName));
    }
  }

  private void visitInflate(JavaContext context, MethodInvocation node) {
  }

  private String camelToUnderscore(String className) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < className.length(); i++) {
      char c = className.charAt(i);
      if (Character.isUpperCase(c) && i > 0) {
        result.append('_');
      }
      result.append(Character.toLowerCase(c));
    }

    return result.toString();
  }
}
