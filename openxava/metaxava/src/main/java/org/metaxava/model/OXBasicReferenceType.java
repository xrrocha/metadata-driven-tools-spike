package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Collection;
import java.util.List;

/**
 * OXBasicReferenceType - Reference types that map to single database columns
 *
 * Examples: Integer, String, BigDecimal, LocalDate, etc.
 *
 * Implements OXBasicType because these map to @Basic JPA properties (single JDBC column).
 *
 * @author MetaXava Reference Types 2025-10-28
 */
@Entity
@DiscriminatorValue("BASIC_REF")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public abstract class OXBasicReferenceType extends OXReferenceType implements OXBasicType {

    /**
     * Associated primitive type (for wrapper types only)
     *
     * Examples:
     * - Integer → int
     * - Boolean → boolean
     * - Character → char
     *
     * NULL for non-wrapper types (String, BigDecimal, LocalDate, etc.)
     */
    @OneToOne
    @JoinColumn(name = "primitive_type_id")
    private OXPrimitiveType primitiveType;

    // OXBasicType implementation - MUST repeat JPA annotations (interfaces don't support them)

    @ManyToMany
    @JoinTable(
        name = "type_jdbc_mappings",
        joinColumns = @JoinColumn(name = "type_id"),
        inverseJoinColumns = @JoinColumn(name = "jdbc_metadata_id")
    )
    private Collection<JDBCTypeMetadata> compatibleJdbcTypes;

    @ManyToOne
    @JoinColumn(name = "preferred_jdbc_type_id")
    private JDBCTypeMetadata preferredJdbcType;

    @Override
    public String getName() {
        return getQualifiedName();
    }

    @Override
    public String generateJavaType() {
        return getSimpleName();
    }

    /**
     * Generate JPA annotations for property declarations
     *
     * Some types require special JPA annotations beyond @Basic:
     * - Temporal types need @Temporal(TemporalType.XXX)
     * - Large objects need @Lob
     * - Enums need @Enumerated
     *
     * This method allows each type to declare its own annotation requirements,
     * keeping generation logic self-contained rather than scattered in generators.
     *
     * @return List of annotation strings (empty if none needed)
     */
    public List<String> generateJPAAnnotations() {
        return List.of();  // Default: no special annotations needed
    }
}
