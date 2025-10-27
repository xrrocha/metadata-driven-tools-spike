application metamodel

// =============================================================================
// METAMODEL ENTITIES - WebDSL Modeling Itself
// =============================================================================

entity DomainApp {
  name : String
  entities : {DomainEntity} (inverse = application)
}

entity DomainEntity {
  name : String
  application -> DomainApp
  properties : {EntityProperty} (inverse = entity)
  relationships : {Relationship} (inverse = sourceEntity)
}

entity EntityProperty {
  name : String
  propertyType : String
  entity -> DomainEntity
}

entity Relationship {
  name : String
  relationshipType : String  // "reference", "collection", "list"
  sourceEntity -> DomainEntity
  targetEntity -> DomainEntity
  inverseName : String       // Empty if unidirectional
}

// Generate CRUD early
derive CRUD DomainApp
derive CRUD DomainEntity
derive CRUD EntityProperty
derive CRUD Relationship

// =============================================================================
// ROOT PAGE
// =============================================================================

page root() {
  title { "Ouroboros Metamodel" }
  
  header { "WebDSL Modeling Itself" }
  
  par { "Model WebDSL applications as data, then generate code." }
  
  par { navigate manageDomainApp() { "Manage Applications" } }
  par { navigate manageDomainEntity() { "Manage Entities" } }
  par { navigate manageEntityProperty() { "Manage Properties" } }
  par { navigate manageRelationship() { "Manage Relationships" } }
  par { navigate codeGenerator() { "Generate Code" } }
  par { navigate bootstrapPage() { "Bootstrap Metamodel (Self-Description)" } }
}

// =============================================================================
// BOOTSTRAP PAGE - Creates self-describing data
// =============================================================================

page bootstrapPage() {
  title { "Bootstrap Metamodel" }
  
  header { "Bootstrap Self-Describing Data" }
  
  par { "This will create entities that describe the metamodel itself." }
  
  var apps := DomainApp.all()
  if(apps.length > 0) {
    par { "Warning: Database already has " output(apps.length) " application(s)." }
    par { "Existing data:" }
    for(theApp in apps) {
      par { "- " output(theApp.name) }
    }
  }
  
  form {
    submit doBootstrap() { "Create Metamodel Self-Description" }
  }
  
  action doBootstrap() {
    // Create Application
    var theApp := DomainApp { name := "metamodel" };
    theApp.save();
    
    // Create Entities
    var entApp := DomainEntity { name := "DomainApp", application := theApp };
    entApp.save();
    
    var entEntity := DomainEntity { name := "DomainEntity", application := theApp };
    entEntity.save();
    
    var entProperty := DomainEntity { name := "EntityProperty", application := theApp };
    entProperty.save();
    
    var entRelationship := DomainEntity { name := "Relationship", application := theApp };
    entRelationship.save();
    
    // Properties for DomainApp
    var propAppName := EntityProperty { 
      name := "name", 
      propertyType := "String", 
      entity := entApp 
    };
    propAppName.save();
    
    // Properties for DomainEntity
    var propEntityName := EntityProperty { 
      name := "name", 
      propertyType := "String", 
      entity := entEntity 
    };
    propEntityName.save();
    
    // Properties for EntityProperty
    var propPropName := EntityProperty { 
      name := "name", 
      propertyType := "String", 
      entity := entProperty 
    };
    propPropName.save();
    
    var propPropType := EntityProperty { 
      name := "propertyType", 
      propertyType := "String", 
      entity := entProperty 
    };
    propPropType.save();
    
    // Properties for Relationship
    var propRelName := EntityProperty { 
      name := "name", 
      propertyType := "String", 
      entity := entRelationship 
    };
    propRelName.save();
    
    var propRelType := EntityProperty { 
      name := "relationshipType", 
      propertyType := "String", 
      entity := entRelationship 
    };
    propRelType.save();
    
    var propRelInverse := EntityProperty { 
      name := "inverseName", 
      propertyType := "String", 
      entity := entRelationship 
    };
    propRelInverse.save();
    
    // Relationships for DomainApp
    var relAppEntities := Relationship {
      name := "entities",
      relationshipType := "collection",
      sourceEntity := entApp,
      targetEntity := entEntity,
      inverseName := "application"
    };
    relAppEntities.save();
    
    // Relationships for DomainEntity
    var relEntityApp := Relationship {
      name := "application",
      relationshipType := "reference",
      sourceEntity := entEntity,
      targetEntity := entApp,
      inverseName := ""
    };
    relEntityApp.save();
    
    var relEntityProps := Relationship {
      name := "properties",
      relationshipType := "collection",
      sourceEntity := entEntity,
      targetEntity := entProperty,
      inverseName := "entity"
    };
    relEntityProps.save();
    
    var relEntityRels := Relationship {
      name := "relationships",
      relationshipType := "collection",
      sourceEntity := entEntity,
      targetEntity := entRelationship,
      inverseName := "sourceEntity"
    };
    relEntityRels.save();
    
    // Relationships for EntityProperty
    var relPropEntity := Relationship {
      name := "entity",
      relationshipType := "reference",
      sourceEntity := entProperty,
      targetEntity := entEntity,
      inverseName := ""
    };
    relPropEntity.save();
    
    // Relationships for Relationship
    var relRelSource := Relationship {
      name := "sourceEntity",
      relationshipType := "reference",
      sourceEntity := entRelationship,
      targetEntity := entEntity,
      inverseName := ""
    };
    relRelSource.save();
    
    var relRelTarget := Relationship {
      name := "targetEntity",
      relationshipType := "reference",
      sourceEntity := entRelationship,
      targetEntity := entEntity,
      inverseName := ""
    };
    relRelTarget.save();
    
    return root();
  }
}

// =============================================================================
// CODE GENERATOR
// =============================================================================

page codeGenerator() {
  title { "Code Generator" }
  
  header { "Generate WebDSL Code" }
  
  var apps := DomainApp.all()
  
  if(apps.length > 0) {
    for(theApp in apps order by theApp.name) {
      par { output(theApp.name) }
      par { navigate viewGeneratedCode(theApp) { "View Code" } }
    }
  } else {
    par { "No applications yet. Create one first." }
  }
}

page viewGeneratedCode(theApp : DomainApp) {
  title { "Generated: " output(theApp.name) }
  
  header { "Generated Code" }
  
  var code := generateApp(theApp)
  
  par { "Save as: " output(theApp.name.toLowerCase()) ".app" }
  
  pre { output(code) }
}

// =============================================================================
// GENERATORS
// =============================================================================

function generateApp(theApp : DomainApp) : String {
  var code := "application " + theApp.name + "\n\n";
  
  var ents := theApp.entities;
  for(e in ents order by e.name) {
    code := code + generateEntity(e) + "\n\n";
  }
  
  for(e in ents order by e.name) {
    code := code + "derive CRUD " + e.name + "\n";
  }
  
  code := code + "\npage root() {\n";
  code := code + "  title { \"" + theApp.name + "\" }\n";
  code := code + "  header { \"Welcome\" }\n";
  for(e in ents order by e.name) {
    code := code + "  par { navigate manage" + e.name + "() { \"Manage " + e.name + "s\" } }\n";
  }
  code := code + "}";
  
  return code;
}

function generateEntity(e : DomainEntity) : String {
  var code := "entity " + e.name + " {\n";
  
  // Generate properties first
  var props := e.properties;
  for(p in props order by p.name) {
    code := code + "  " + p.name + " : " + p.propertyType + "\n";
  }
  
  // Generate relationships
  var rels := e.relationships;
  for(r in rels order by r.name) {
    code := code + generateRelationship(r);
  }
  
  code := code + "}";
  return code;
}

function generateRelationship(r : Relationship) : String {
  var code := "  " + r.name + " ";
  
  if(r.relationshipType == "reference") {
    code := code + "-> " + r.targetEntity.name;
  } else if(r.relationshipType == "collection") {
    code := code + ": {" + r.targetEntity.name + "}";
  } else if(r.relationshipType == "list") {
    code := code + ": [" + r.targetEntity.name + "]";
  }
  
  // Add inverse if bidirectional
  if(r.inverseName != "") {
    code := code + " (inverse = " + r.inverseName + ")";
  }
  
  code := code + "\n";
  return code;
}
