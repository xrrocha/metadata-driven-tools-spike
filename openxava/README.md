# OpenXava Exploration

This directory will contain OpenXava installation, applications, and experiments for the metadata-driven-tools-spike project.

## Goal

Explore OpenXava as an alternative/complementary platform for metadata-driven tool development, with the aim of building an OpenXava application that can describe and generate other OpenXava applications (Ouroboros principle).

## Why OpenXava?

- **Lower barrier to entry**: Java-based, proven in production
- **Inspectable & extensible**: Source code available for study and contribution
- **Rich ecosystem**: Standard JEE stack with excellent tooling
- **Annotation-driven**: Simpler UI generation compared to WebDSL's full DSL
- **Active community**: More resources and examples available

## Research Questions

1. Can OpenXava's metamodel be simpler to capture than WebDSL's?
2. How does JPA's type system affect metamodel complexity?
3. Can we build "MetaXava" - an OpenXava app that generates OpenXava apps?
4. Is this a better platform for the filer application use case?

## Exploration Phases

### Phase 1: Documentation & Architecture Analysis
- Study official OpenXava docs
- Analyze annotation-driven generation
- Map metamodel structure

### Phase 2: Codebase Study
- Clone and explore source code
- Trace entity → UI generation pipeline
- Understand extension points

### Phase 3: Metamodel Extraction
- Document what metadata OpenXava uses
- Compare to WebDSL's approach
- Design MetaXava entities

### Phase 4: Proof of Concept
- Build MetaXava application
- Generate simple OpenXava app from metadata
- Test Ouroboros closure

### Phase 5: Evaluation
- Compare with WebDSL findings
- Assess production readiness
- Decide on platform for filer tools

## Status

**Not yet started** - Exploration begins here

## See Also

- [OpenXava Official Site](https://www.openxava.org/)
- [Project Root README](../README.md)
- [WebDSL Exploration](../webdsl/)
