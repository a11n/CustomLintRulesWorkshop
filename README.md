# CustomLintRules

This workshop was held at [Droidcon Berlin](http://droidcon.de) on June 3rd, 2015.

This repository contains sources and examples covering the **"Hands-on" part**.

Please find the complete workshop material [here](http://a11n.github.io/lint-workshop-slides).

## Motivation
Android Lint was introduced back in December 2011 [1]. Since then its IDE integration became better and better. Today Lint is the Android developer’s essential smart companion guaranteeing a good quality of all Android development assets. With the release of the most recent Android Studio version in January 2015 Lint features more than 200 default checks [2]. It checks for potential bugs, bad coding habits, broken conventions and much more.

However, several circumstances exist where it is necessary to extend the default set with **custom Lint rules**. This counts in particular for large projects with either a huge code base or big, distributed developer teams. Anyways, it is good practice to have additional team-, project- or company-wide conventions. But how to enforce them?

## Objective
Although Lint is a fundamental part of the Android Developer Tools, the documentation on how to write custom Lint rules is **rare** [3] and **deprecated** [4]. Therefore, the objective of this workshop is to highlight the creation of custom Lint rules. It will showcase the usage of the most recent Lint API [5] and it will demonstrate how to write different types of rules (e.g. code-, resource-, project-structure-related). It will also present how to build custom rules with Gradle and even how to bundle them with your app project (which improves the integration into CI environments).

Furthermore, it will provide some insights of useful conventions in real-world scenarios we had to struggle with and where custom Lint rules saved a lot of time and money.

At the end, participants will be prepared and encouraged to write their own custom Lint rules.

## Prerequisites
In order to checkout and compile artifacts related to this workshop some tools are required:

1. Git
2. Java 7
3. Gradle
4. Docker
5. Android Studio (optional)

## Structure
The workshop is structured into **5** consecutive sections.

To start, just do:
```shell
git clone https://github.com/a11n/CustomLintRulesWorkshop.git
cd CustomLintRulesWorkshop
git checkout -f section-1
```

In order to move from one section to another execute:
```shell
git checkout -f section-N #where N is your current section number+1
```

### Section 1
A basic project template to write custom Lint rules.

### Section 2
Showcase of simple detectors.

### Section 3
Demonstration of advanced detecting techniques.

### Section 4
Testing custom Lint rules.

### Section 5
Custom Lint rules and continuous integration.

## Remark
As Google points out very significant, the Lint API **is not final and may change in future releases** [3].

This workshop refers to the most recent (May 2015), stable version (24.3.0-beta1) of the API [5].

## References
1. http://tools.android.com/tips/lint (visited 2015-05-01)
2. http://tools.android.com/tips/lint-checks (visited 2015-05-01)
3. http://tools.android.com/tips/lint/writing-a-lint-check (visited 2015-05-01)
4. http://tools.android.com/tips/lint-custom-rules (visited 2015-05-01)
5. https://bintray.com/android/android-tools/com.android.tools.lint.lint-api/view (visited 2015-05-28)
6. https://engineering.linkedin.com/android/writing-custom-lint-checks-gradle (visited 2015-05-01)
7. https://android.googlesource.com/platform/tools/base/+/master/lint/libs/lint-tests/src/main/java/com/android/tools/lint/checks/infrastructure/LintDetectorTest.java (visited 2015-05-01)
