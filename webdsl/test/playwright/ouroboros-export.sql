-- Ouroboros Metamodel SQL Export
-- Generated: 2025-10-26T17:56:36.276Z
-- Extracted via web scraping from WebDSL UI
-- Target: PostgreSQL

BEGIN;

INSERT INTO DomainApp (name) VALUES ('metamodel');
INSERT INTO DomainApp (name) VALUES ('metamodel');

INSERT INTO DomainEntity (name, application_id) VALUES ('DomainApp', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('DomainEntity', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('EntityProperty', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('Relationship', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('ValidationRule', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('DerivedProperty', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('EntityFunction', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('Page', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('PageElement', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('DomainApp', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('DomainEntity', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('EntityProperty', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('Relationship', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('ValidationRule', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('DerivedProperty', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('EntityFunction', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('Page', (SELECT id FROM DomainApp WHERE name = 'metamodel'));
INSERT INTO DomainEntity (name, application_id) VALUES ('PageElement', (SELECT id FROM DomainApp WHERE name = 'metamodel'));

INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'DomainApp'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('propertyType', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('relationshipType', 'String', (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('inverseName', 'String', (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('expression', 'String', (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('message', 'String', (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('propertyType', 'String', (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('expression', 'String', (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('returnType', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('body', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'Page'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('elementType', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('content', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('navigateTarget', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('navigateLabel', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('orderIndex', 'Int', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'DomainApp'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('propertyType', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('relationshipType', 'String', (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('inverseName', 'String', (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('expression', 'String', (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('message', 'String', (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('propertyType', 'String', (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('expression', 'String', (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('returnType', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('body', 'String', (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('name', 'String', (SELECT id FROM DomainEntity WHERE name = 'Page'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('elementType', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('content', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('navigateTarget', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('navigateLabel', 'String', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES ('orderIndex', 'Int', (SELECT id FROM DomainEntity WHERE name = 'PageElement'));

INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entities', 'DomainEntity', 'collection', 'application', (SELECT id FROM DomainEntity WHERE name = 'DomainApp'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('pages', 'Page', 'collection', 'application', (SELECT id FROM DomainEntity WHERE name = 'DomainApp'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('application', 'DomainApp', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('properties', 'EntityProperty', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('relationships', 'Relationship', 'collection', 'sourceEntity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('validationRules', 'ValidationRule', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('derivedProperties', 'DerivedProperty', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('functions', 'EntityFunction', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'EntityProperty'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('sourceEntity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('targetEntity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('application', 'DomainApp', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'Page'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('elements', 'PageElement', 'collection', 'page', (SELECT id FROM DomainEntity WHERE name = 'Page'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('page', 'Page', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'PageElement'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entities', 'DomainEntity', 'collection', 'application', (SELECT id FROM DomainEntity WHERE name = 'DomainApp'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('pages', 'Page', 'collection', 'application', (SELECT id FROM DomainEntity WHERE name = 'DomainApp'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('application', 'DomainApp', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('properties', 'EntityProperty', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('relationships', 'Relationship', 'collection', 'sourceEntity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('validationRules', 'ValidationRule', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('derivedProperties', 'DerivedProperty', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('functions', 'EntityFunction', 'collection', 'entity', (SELECT id FROM DomainEntity WHERE name = 'DomainEntity'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'EntityProperty'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('sourceEntity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('targetEntity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'Relationship'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'ValidationRule'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'DerivedProperty'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('entity', 'DomainEntity', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'EntityFunction'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('application', 'DomainApp', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'Page'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('elements', 'PageElement', 'collection', 'page', (SELECT id FROM DomainEntity WHERE name = 'Page'));
INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES ('page', 'Page', 'reference', NULL, (SELECT id FROM DomainEntity WHERE name = 'PageElement'));






COMMIT;

-- Verify counts
SELECT 'DomainApp' as table_name, COUNT(*) as count FROM DomainApp
UNION ALL SELECT 'DomainEntity', COUNT(*) FROM DomainEntity
UNION ALL SELECT 'EntityProperty', COUNT(*) FROM EntityProperty
UNION ALL SELECT 'Relationship', COUNT(*) FROM Relationship
UNION ALL SELECT 'ValidationRule', COUNT(*) FROM ValidationRule
UNION ALL SELECT 'DerivedProperty', COUNT(*) FROM DerivedProperty
UNION ALL SELECT 'EntityFunction', COUNT(*) FROM EntityFunction
UNION ALL SELECT 'Page', COUNT(*) FROM Page
UNION ALL SELECT 'PageElement', COUNT(*) FROM PageElement;

-- 🐍 Ouroboros complete! The snake has captured its own tail in SQL form.
