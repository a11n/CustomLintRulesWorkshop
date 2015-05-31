package de.droidcon.workshop.lint;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Collection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static com.android.SdkConstants.CHECK_BOX;
import static com.android.SdkConstants.EDIT_TEXT;
import static com.android.SdkConstants.IMAGE_BUTTON;
import static com.android.SdkConstants.IMAGE_VIEW;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.LIST_VIEW;
import static com.android.SdkConstants.PROGRESS_BAR;
import static com.android.SdkConstants.RADIO_BUTTON;
import static com.android.SdkConstants.TEXT_VIEW;

/**
 * Checks widgets ids to match a certain pattern.
 *
 * Pattern: widgetAbbreviation + widgetName
 * Example: tvCustomerName (TextView)
 *          btSubmit (Button)
 */
public class IdPrefixDetector extends LayoutDetector {
  public static final Issue ISSUE = Issue.create(
      "ViewId",
      "Invalid view id",
      "View id must be prefixed by view class abbreviation.\n"
          + "Example: TextView -> tvCustomerName",
      Category.CORRECTNESS,
      5,
      Severity.WARNING,
      new Implementation(
          IdPrefixDetector.class,
          Scope.RESOURCE_FILE_SCOPE)
  );

  @Override public Collection<String> getApplicableElements() {
    return XmlScanner.ALL;
  }

  @Override public void visitElement(XmlContext context, Element element) {
    Node id = element.getAttributes().getNamedItem("android:id");

    if (id == null) return;

    String viewType = element.getLocalName();
    String viewId = LintUtils.stripIdPrefix(id.getNodeValue());

    switch (viewType) {
      case TEXT_VIEW:
        if (!viewId.startsWith("tv")) report(context, element, viewType, viewId, "tv");
        break;
      case EDIT_TEXT:
        if (!viewId.startsWith("et")) report(context, element, viewType, viewId, "et");
        break;
      case IMAGE_VIEW:
        if (!viewId.startsWith("iv")) report(context, element, viewType, viewId, "iv");
        break;
      case IMAGE_BUTTON:
        if (!viewId.startsWith("ib")) report(context, element, viewType, viewId, "ib");
        break;
      case LINEAR_LAYOUT:
        if (!viewId.startsWith("ll")) report(context, element, viewType, viewId, "ll");
        break;
      case LIST_VIEW:
        if (!viewId.startsWith("lv")) report(context, element, viewType, viewId, "lv");
        break;
      case PROGRESS_BAR:
        if (!viewId.startsWith("pb")) report(context, element, viewType, viewId, "pb");
        break;
      case RADIO_BUTTON:
        if (!viewId.startsWith("rb")) report(context, element, viewType, viewId, "rb");
        break;
      case CHECK_BOX:
        if (!viewId.startsWith("cb")) report(context, element, viewType, viewId, "cb");
        break;
      default:
        System.out.println(element.getLocalName());
    }
  }

  private void report(XmlContext context, Element element, String type, String viewId,
      String expectedPrefix) {
    context.report(ISSUE, context.getLocation(element),
        String.format("View '%s' of type %s must be prefixed with '%s'", viewId, type,
            expectedPrefix));
  }
}
