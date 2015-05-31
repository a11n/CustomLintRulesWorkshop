package de.droidcon.workshop.lint;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Arrays;
import java.util.Collection;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import static com.android.SdkConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.SdkConstants.ATTR_HINT;
import static com.android.SdkConstants.ATTR_LABEL;
import static com.android.SdkConstants.ATTR_PROMPT;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.SdkConstants.STRING_PREFIX;

/**
 * Checks string reference names to match a certain pattern.
 *
 * Pattern: layoutName_viewId_attributeName
 * Example: activity_main_tvHello_text
 */
public class StringReferencesDetector extends LayoutDetector {
  public static final Issue ISSUE = Issue.create(
      "StringName",
      "Invalid string name",
      "String names must be of pattern layoutName_viewId_attributeName.\n"
          + "Example: activity_main_tvHello_text",
      Category.CORRECTNESS,
      5,
      Severity.WARNING,
      new Implementation(
          StringReferencesDetector.class,
          Scope.RESOURCE_FILE_SCOPE)
  );

  @Override public Collection<String> getApplicableAttributes() {
    return Arrays.asList(ATTR_TEXT, ATTR_CONTENT_DESCRIPTION, ATTR_HINT, ATTR_LABEL, ATTR_PROMPT);
  }

  @Override public void visitAttribute(XmlContext context, Attr attribute) {
    String layoutName = LintUtils.getLayoutName(context.file);

    Node id = attribute.getOwnerElement().getAttributes().getNamedItem("android:id");

    if (id == null) return;

    String viewId = LintUtils.stripIdPrefix(id.getNodeValue());
    String attributeName = attribute.getName().replace("android:", "");

    String expected = String.format("%s_%s_%s", layoutName, viewId, attributeName);
    String actual = attribute.getValue();

    //hardcoded value detection
    if (!actual.startsWith(STRING_PREFIX)) {
      context.report(ISSUE, attribute, context.getLocation(attribute),
          String.format("String value '%s' is not a reference. Should start with '%s'.", actual,
              STRING_PREFIX));
      return;
    }

    actual = actual.substring(STRING_PREFIX.length(), actual.length());

    if (!actual.equals(expected)) {
      context.report(ISSUE, attribute, context.getLocation(attribute),
          String.format("Invalid string name '%s'. '%s' expected.", actual, expected));
      return;
    }
  }
}
