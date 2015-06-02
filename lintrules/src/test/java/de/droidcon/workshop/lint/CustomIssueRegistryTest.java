package de.droidcon.workshop.lint;

import com.android.tools.lint.detector.api.Issue;
import de.droidcon.workshop.lint.detectors.advanced.HelloWorldDetector;
import de.droidcon.workshop.lint.detectors.simple.ClassNamesDetector;
import de.droidcon.workshop.lint.detectors.simple.IdPrefixDetector;
import de.droidcon.workshop.lint.detectors.simple.LayoutNamesDetector;
import de.droidcon.workshop.lint.detectors.simple.StringReferencesDetector;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomIssueRegistryTest {
  @Test public void testGetIssues() throws Exception {
    CustomIssueRegistry customIssueRegistry = new CustomIssueRegistry();

    List<Issue> actual = customIssueRegistry.getIssues();

    assertThat(actual).containsExactly(
        HelloWorldDetector.ISSUE,
        IdPrefixDetector.ISSUE,
        StringReferencesDetector.ISSUE,
        ClassNamesDetector.ISSUE,
        LayoutNamesDetector.ISSUE
    );
  }
}