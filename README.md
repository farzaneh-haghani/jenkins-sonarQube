# Software Craft Starter

Deliberately bad-but-working Java application for a Software Craft module.

## Local commands

```bash
mvn clean compile
mvn test
mvn exec:java
```

## Quality reports

```bash
mvn jacoco:report
mvn checkstyle:checkstyle
mvn pmd:pmd
mvn spotbugs:spotbugs
```

Reports appear under:

```text
target/site/
```

## SonarQube

```bash
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=YOUR_TOKEN
```

## Teaching flow

1. Run app.
2. Run tests.
3. Run Jenkins pipeline.
4. Add Sonar/static analysis.
5. Review `OrderProcessor` smells.
6. Refactor in pairs.
7. Introduce Strategy/Factory/Adapter only where useful.
8. Re-run pipeline and compare.
