# WebDSL Exploration

This directory contains the WebDSL installation, applications, and experiments for the metadata-driven-tools-spike project.

## Directory Structure

- **`compiler/`** - WebDSL compiler installation (binary distribution)
- **`hello-webdsl/`** - Basic "Hello World" WebDSL application
- **`library/`** - WebDSL application library
- **`metamodel/`**, **`metamodel-v2/`**, **`metamodel-v3/`** - Progressive versions of the Ouroboros metamodel (self-describing system)
- **`scott/`**, **`test/`**, **`testapp/`**, **`test-phase2/`** - Test and experimental applications
- **`*.sh`** - Utility scripts for code generation and application management
- **`metamodel-comprehensive-design.app`** - Design document for the comprehensive metamodel

## Running WebDSL Applications

```bash
export PATH="$HOME/.sdkman/candidates/ant/current/bin:$PATH"
cd hello-webdsl
../compiler/bin/webdsl run hello
```

Access at: http://localhost:8080/hello

## Quick Commands

**Compile only:**
```bash
../compiler/bin/webdsl compile hello
```

**Clean build:**
```bash
rm -rf .servletapp
../compiler/bin/webdsl run hello
```

## About WebDSL

WebDSL is a domain-specific language for developing web applications with a rich data model. This exploration validates the Ouroboros Principle - building a system that can model itself and generate its own modeling tools.

## See Also

- [WebDSL Official Docs](https://webdsl.org/reference/)
- [Project Root README](../README.md)
- [OpenXava Exploration](../openxava/)
