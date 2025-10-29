# Metadata-Driven Tools Spike

Research spike exploring metadata-driven approaches for building self-describing software systems. Originally started as a filer spike, now expanded to compare WebDSL and OpenXava as platforms for the **Ouroboros Principle**: systems that can model themselves and generate tools for modeling.

## Project Structure

```
metadata-driven-tools-spike/
├── webdsl/           # WebDSL exploration and applications
└── openxava/         # OpenXava exploration (current focus)
```

## Explorations

### WebDSL
Domain-specific language for web applications with rich data modeling. See [webdsl/README.md](webdsl/README.md).

**Status**: Active - bootstrapped metamodel, validated Ouroboros closure

### OpenXava
Java-based model-driven framework for business applications. See [openxava/README.md](openxava/README.md).

**Status**: Not started - planned exploration

## Philosophy

1. **Self-Description**: Formalism can model its own constructs
2. **Organic Growth**: Start simple, add incrementally, preserve closure
3. **Empirical Validation**: Measure automation, test hypotheses
4. **Code Generation**: Generate from formal models, not hand-code
5. **Research Focus**: Discover limits, document failures and successes

## Goals

- Build metadata-driven tools for file management (filer application)
- Compare WebDSL vs OpenXava for production use
- Validate which platform better supports the Ouroboros Principle
- Create tools that generate modeling tools

## Resources

- [WebDSL Official Docs](https://webdsl.org/reference/)
- [OpenXava Official Site](https://www.openxava.org/)
- [OpenXava Documentation](https://www.openxava.org/docs/en/)
- [WebDSL GitHub](https://github.com/webdsl/webdsl)
- [OpenXava GitHub](https://github.com/openxava/openxava)
