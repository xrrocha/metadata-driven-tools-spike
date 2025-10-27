package org.metaxava.run;

import org.openxava.util.*;

/**
 * MetaXava - OpenXava Metamodel Generator
 *
 * This application models OpenXava applications themselves (Ouroboros principle).
 * It uses Java 25 features for the generator while allowing generated code
 * to target any Java version OpenXava supports (8, 11, 17, 21, 25).
 *
 * DESIGN STRATEGY:
 *
 * 1. Generator (this project): Java 25
 *    - Records for immutable metamodel data
 *    - Pattern matching for type discrimination
 *    - Text blocks for code generation templates
 *    - Sealed types for type hierarchies
 *
 * 2. Generated code: Configurable target version
 *    - Users specify target Java version in MetaXava configuration
 *    - Code generator adapts syntax accordingly
 *    - Example: Records (Java 14+) vs traditional classes (Java 8)
 *
 * 3. Key architectural decisions (from architecture session 2025-10-27):
 *    - No stringly-typed properties (types as entities)
 *    - Surgical precision (model JPA's type system rigorously)
 *    - Property specialization hierarchy
 *    - Primitives ≠ Wrappers (distinct types)
 *    - Design considerations in code comments
 *    - Optional<T> for optionality (NEVER null)
 */
public class MetaXava {

	public static void main(String[] args) throws Exception {
		DBServer.start("metaxava-db");
		AppServer.run("metaxava"); // Use AppServer.run() to deploy in an internal web server
	}

}
