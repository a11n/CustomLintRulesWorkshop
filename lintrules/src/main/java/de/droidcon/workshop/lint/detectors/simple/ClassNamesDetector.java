package de.droidcon.workshop.lint.detectors.simple;

import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import java.util.Arrays;
import java.util.List;
import lombok.ast.ClassDeclaration;
import lombok.ast.Node;

import static com.android.SdkConstants.CLASS_ACTIVITY;
import static com.android.SdkConstants.CLASS_FRAGMENT;
import static com.android.SdkConstants.CLASS_V4_FRAGMENT;

/**
 * Checks Activity/Fragment class names to match a certain pattern.
 *
 * Pattern: [Activity|Fragment]ClassName
 *
 * Examples:
 * ------------
 * ActivityOrderStatus (if Class<? extends Activity>)
 * or FragmentOrderDetails (if Class<? extends Fragment>)
 *
 */
public class ClassNamesDetector extends Detector implements Detector.JavaScanner {

  public static final Issue CLASS_NAME_ISSUE = Issue.create("ClassName", "Invalid class name",
      "Classes which extend Activity or Fragment must be named with pattern [Activity|Fragment]ClassName.\n"
          + "Example: ActivityOrderStatus", Category.CORRECTNESS, 5, Severity.WARNING,
      new Implementation(ClassNamesDetector.class, Scope.JAVA_FILE_SCOPE));

  @Override public List<String> applicableSuperClasses() {
    return Arrays.asList(
      CLASS_ACTIVITY,
      CLASS_FRAGMENT,
      CLASS_V4_FRAGMENT
    );
  }

  @Override public void checkClass(JavaContext context, ClassDeclaration declaration, Node node,
      JavaParser.ResolvedClass resolvedClass) {
    if (resolvedClass.isSubclassOf(CLASS_ACTIVITY, false)) {
      checkActivityClassName(context, context.getLocation(node), resolvedClass.getSimpleName());
    } else if (resolvedClass.isSubclassOf(CLASS_FRAGMENT, false) || resolvedClass.isSubclassOf(
        CLASS_V4_FRAGMENT, false)) {
      checkFragmentClassName(context, context.getLocation(node), resolvedClass.getSimpleName());
    }
  }

  private void checkActivityClassName(Context context, Location location, String className) {
    if (!className.startsWith("Activity")) {
      context.report(CLASS_NAME_ISSUE, location,
          String.format("Activity '%s' should be of pattern 'ActivityClassName'.", className));
    }
  }

  private void checkFragmentClassName(Context context, Location location, String className) {
    if (!className.startsWith("Fragment")) {
      context.report(CLASS_NAME_ISSUE, location,
          String.format("Fragment '%s' should be of pattern 'FragmentClassName'.", className));
    }
  }
}
