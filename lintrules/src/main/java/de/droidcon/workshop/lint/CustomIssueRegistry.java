package de.droidcon.workshop.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import de.droidcon.workshop.lint.detectors.simple.IdPrefixDetector;
import de.droidcon.workshop.lint.detectors.simple.StringReferencesDetector;
import java.util.Arrays;
import java.util.List;

public class CustomIssueRegistry extends IssueRegistry {
  @Override public List<Issue> getIssues() {
    return Arrays.asList(
        HelloWorldDetector.ISSUE,
        IdPrefixDetector.ISSUE,
        StringReferencesDetector.ISSUE
    );
  }
}