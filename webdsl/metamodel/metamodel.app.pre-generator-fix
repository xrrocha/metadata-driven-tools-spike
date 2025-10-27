application metamodel



// =============================================================================
// PHASE 1: STRUCTURAL ENTITIES
// =============================================================================

entity DomainApp {
  name : String
  entities : {DomainEntity} (inverse = application)
  pages : {Page} (inverse = application)  // Phase 3: UI
}

entity DomainEntity {
  name : String
  application -> DomainApp
  properties : {EntityProperty} (inverse = entity)
  relationships : {Relationship} (inverse = sourceEntity)
  
  // Phase 2: Behavioral
  validationRules : {ValidationRule} (inverse = entity)
  derivedProperties : {DerivedProperty} (inverse = entity)
  functions : {EntityFunction} (inverse = entity)
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

// =============================================================================
// PHASE 2: BEHAVIORAL ENTITIES
// =============================================================================

entity ValidationRule {
  name : String
  expression : String  // e.g., "age >= 18"
  message : String     // e.g., "Must be 18 or older"
  entity -> DomainEntity
}

entity DerivedProperty {
  name : String
  propertyType : String
  expression : String  // e.g., "firstName + ' ' + lastName"
  entity -> DomainEntity
}

entity EntityFunction {
  name : String
  returnType : String  // Empty string if void
  body : String        // Function body as text
  entity -> DomainEntity
}

// =============================================================================
// PHASE 3: UI/PAGES ENTITIES
// =============================================================================

entity Page {
  name : String
  application -> DomainApp
  elements : {PageElement} (inverse = page)
}

entity PageElement {
  elementType : String  // "title", "header", "par", "navigate"
  content : String      // Text content
  navigateTarget : String  // Page name for navigate elements
  navigateLabel : String   // Link text for navigate elements
  page -> Page
  orderIndex : Int      // For ordering elements
}

// Generate CRUD for all entities
derive CRUD DomainApp
derive CRUD DomainEntity
derive CRUD EntityProperty
derive CRUD Relationship
derive CRUD ValidationRule
derive CRUD DerivedProperty
derive CRUD EntityFunction
derive CRUD Page
derive CRUD PageElement

// =============================================================================
// ROOT PAGE
// =============================================================================

page root() {
  title { "Ouroboros Metamodel" }
  
  header { "WebDSL Modeling Itself" }
  
  par { "Model WebDSL applications as data, then generate code." }
  
  header { "Phase 1: Structural" }
  par { navigate manageDomainApp() { "Manage Applications" } }
  par { navigate manageDomainEntity() { "Manage Entities" } }
  par { navigate manageEntityProperty() { "Manage Properties" } }
  par { navigate manageRelationship() { "Manage Relationships" } }
  
  header { "Phase 2: Behavioral" }
  par { navigate manageValidationRule() { "Manage ValidationRules" } }
  par { navigate manageDerivedProperty() { "Manage DerivedPropertys" } }
  par { navigate manageEntityFunction() { "Manage EntityFunctions" } }
  
  header { "Phase 3: UI/Pages" }
  par { navigate managePage() { "Manage Pages" } }
  par { navigate managePageElement() { "Manage PageElements" } }
  
  header { "Actions" }
  par { navigate codeGenerator() { "Generate Code" } }
  par { navigate bootstrapPage() { "Bootstrap Metamodel (Self-Description)" } }
}

// =============================================================================
// BOOTSTRAP PAGE - Creates self-describing data
// =============================================================================

page bootstrapPage() {
  title { "Bootstrap Metamodel" }
  
  header { "Bootstrap Self-Describing Data" }
  
  par { "This will create entities that describe the metamodel itself (Phase 1 + Phase 2 + Phase 3)." }
  
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
    
    // Create Entities (Phase 1)
    var entApp := DomainEntity { name := "DomainApp", application := theApp };
    entApp.save();
    
    var entEntity := DomainEntity { name := "DomainEntity", application := theApp };
    entEntity.save();
    
    var entProperty := DomainEntity { name := "EntityProperty", application := theApp };
    entProperty.save();
    
    var entRelationship := DomainEntity { name := "Relationship", application := theApp };
    entRelationship.save();
    
    // Create Entities (Phase 2)
    var entValidation := DomainEntity { name := "ValidationRule", application := theApp };
    entValidation.save();
    
    var entDerived := DomainEntity { name := "DerivedProperty", application := theApp };
    entDerived.save();
    
    var entFunction := DomainEntity { name := "EntityFunction", application := theApp };
    entFunction.save();
    
    // Create Entities (Phase 3)
    var entPage := DomainEntity { name := "Page", application := theApp };
    entPage.save();
    
    var entPageElement := DomainEntity { name := "PageElement", application := theApp };
    entPageElement.save();
    
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
    
    // Properties for ValidationRule
    var propValName := EntityProperty {
      name := "name",
      propertyType := "String",
      entity := entValidation
    };
    propValName.save();
    
    var propValExpr := EntityProperty {
      name := "expression",
      propertyType := "String",
      entity := entValidation
    };
    propValExpr.save();
    
    var propValMsg := EntityProperty {
      name := "message",
      propertyType := "String",
      entity := entValidation
    };
    propValMsg.save();
    
    // Properties for DerivedProperty
    var propDerName := EntityProperty {
      name := "name",
      propertyType := "String",
      entity := entDerived
    };
    propDerName.save();
    
    var propDerType := EntityProperty {
      name := "propertyType",
      propertyType := "String",
      entity := entDerived
    };
    propDerType.save();
    
    var propDerExpr := EntityProperty {
      name := "expression",
      propertyType := "String",
      entity := entDerived
    };
    propDerExpr.save();
    
    // Properties for EntityFunction
    var propFuncName := EntityProperty {
      name := "name",
      propertyType := "String",
      entity := entFunction
    };
    propFuncName.save();
    
    var propFuncReturn := EntityProperty {
      name := "returnType",
      propertyType := "String",
      entity := entFunction
    };
    propFuncReturn.save();
    
    var propFuncBody := EntityProperty {
      name := "body",
      propertyType := "String",
      entity := entFunction
    };
    propFuncBody.save();
    
    // Properties for Page (Phase 3)
    var propPageName := EntityProperty {
      name := "name",
      propertyType := "String",
      entity := entPage
    };
    propPageName.save();
    
    // Properties for PageElement (Phase 3)
    var propElemType := EntityProperty {
      name := "elementType",
      propertyType := "String",
      entity := entPageElement
    };
    propElemType.save();
    
    var propElemContent := EntityProperty {
      name := "content",
      propertyType := "String",
      entity := entPageElement
    };
    propElemContent.save();
    
    var propElemNavTarget := EntityProperty {
      name := "navigateTarget",
      propertyType := "String",
      entity := entPageElement
    };
    propElemNavTarget.save();
    
    var propElemNavLabel := EntityProperty {
      name := "navigateLabel",
      propertyType := "String",
      entity := entPageElement
    };
    propElemNavLabel.save();
    
    var propElemOrder := EntityProperty {
      name := "orderIndex",
      propertyType := "Int",
      entity := entPageElement
    };
    propElemOrder.save();
    
    // Relationships for DomainApp
    var relAppEntities := Relationship {
      name := "entities",
      relationshipType := "collection",
      sourceEntity := entApp,
      targetEntity := entEntity,
      inverseName := "application"
    };
    relAppEntities.save();
    
    // Phase 3: DomainApp -> Page relationship
    var relAppPages := Relationship {
      name := "pages",
      relationshipType := "collection",
      sourceEntity := entApp,
      targetEntity := entPage,
      inverseName := "application"
    };
    relAppPages.save();
    
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
    
    // Phase 2: Behavioral relationships for DomainEntity
    var relEntityVals := Relationship {
      name := "validationRules",
      relationshipType := "collection",
      sourceEntity := entEntity,
      targetEntity := entValidation,
      inverseName := "entity"
    };
    relEntityVals.save();
    
    var relEntityDerived := Relationship {
      name := "derivedProperties",
      relationshipType := "collection",
      sourceEntity := entEntity,
      targetEntity := entDerived,
      inverseName := "entity"
    };
    relEntityDerived.save();
    
    var relEntityFuncs := Relationship {
      name := "functions",
      relationshipType := "collection",
      sourceEntity := entEntity,
      targetEntity := entFunction,
      inverseName := "entity"
    };
    relEntityFuncs.save();
    
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
    
    // Relationships for ValidationRule
    var relValEntity := Relationship {
      name := "entity",
      relationshipType := "reference",
      sourceEntity := entValidation,
      targetEntity := entEntity,
      inverseName := ""
    };
    relValEntity.save();
    
    // Relationships for DerivedProperty
    var relDerEntity := Relationship {
      name := "entity",
      relationshipType := "reference",
      sourceEntity := entDerived,
      targetEntity := entEntity,
      inverseName := ""
    };
    relDerEntity.save();
    
    // Relationships for EntityFunction
    var relFuncEntity := Relationship {
      name := "entity",
      relationshipType := "reference",
      sourceEntity := entFunction,
      targetEntity := entEntity,
      inverseName := ""
    };
    relFuncEntity.save();
    
    // Relationships for Page (Phase 3)
    var relPageApp := Relationship {
      name := "application",
      relationshipType := "reference",
      sourceEntity := entPage,
      targetEntity := entApp,
      inverseName := ""
    };
    relPageApp.save();
    
    var relPageElements := Relationship {
      name := "elements",
      relationshipType := "collection",
      sourceEntity := entPage,
      targetEntity := entPageElement,
      inverseName := "page"
    };
    relPageElements.save();
    
    // Relationships for PageElement (Phase 3)
    var relElemPage := Relationship {
      name := "page",
      relationshipType := "reference",
      sourceEntity := entPageElement,
      targetEntity := entPage,
      inverseName := ""
    };
    relElemPage.save();
    

    // =========================================================================
    // PHASE 2: CREATE VALIDATION RULES, DERIVED PROPERTIES, ENTITY FUNCTIONS
    // =========================================================================

    // Validation Rules
    // ----------------

    // DomainApp: name must not be empty
    var valAppName := ValidationRule {
      name := "nameNotEmpty",
      expression := "name != \"\"",
      message := "Application name is required",
      entity := entApp
    };
    valAppName.save();

    // DomainEntity: name must not be empty
    var valEntityName := ValidationRule {
      name := "nameNotEmpty",
      expression := "name != \"\"",
      message := "Entity name is required",
      entity := entEntity
    };
    valEntityName.save();

    // EntityProperty: name must not be empty
    var valPropName := ValidationRule {
      name := "nameNotEmpty",
      expression := "name != \"\"",
      message := "Property name is required",
      entity := entProperty
    };
    valPropName.save();

    // EntityProperty: propertyType must not be empty
    var valPropType := ValidationRule {
      name := "typeNotEmpty",
      expression := "propertyType != \"\"",
      message := "Property type is required",
      entity := entProperty
    };
    valPropType.save();

    // Relationship: name must not be empty
    var valRelName := ValidationRule {
      name := "nameNotEmpty",
      expression := "name != \"\"",
      message := "Relationship name is required",
      entity := entRelationship
    };
    valRelName.save();

    // Derived Properties
    // ------------------

    // DomainEntity: count properties
    var derEntityPropCount := DerivedProperty {
      name := "propertyCount",
      propertyType := "Int",
      expression := "properties.length",
      entity := entEntity
    };
    derEntityPropCount.save();

    // DomainEntity: count relationships
    var derEntityRelCount := DerivedProperty {
      name := "relationshipCount",
      propertyType := "Int",
      expression := "relationships.length",
      entity := entEntity
    };
    derEntityRelCount.save();

    // DomainEntity: count validation rules
    var derEntityValCount := DerivedProperty {
      name := "validationRuleCount",
      propertyType := "Int",
      expression := "validationRules.length",
      entity := entEntity
    };
    derEntityValCount.save();

    // DomainApp: count entities
    var derAppEntityCount := DerivedProperty {
      name := "entityCount",
      propertyType := "Int",
      expression := "entities.length",
      entity := entApp
    };
    derAppEntityCount.save();

    // Page: count elements
    var derPageElemCount := DerivedProperty {
      name := "elementCount",
      propertyType := "Int",
      expression := "elements.length",
      entity := entPage
    };
    derPageElemCount.save();

    // Entity Functions
    // ----------------

    // DomainEntity: display name with prefix
    var funcEntityDisplay := EntityFunction {
      name := "displayName",
      returnType := "String",
      body := "return \"Entity: \" + name;",
      entity := entEntity
    };
    funcEntityDisplay.save();

    // DomainEntity: check if entity has properties
    var funcEntityHasProps := EntityFunction {
      name := "hasProperties",
      returnType := "Bool",
      body := "return properties.length > 0;",
      entity := entEntity
    };
    funcEntityHasProps.save();

    // DomainApp: get entity by name
    var funcAppGetEntity := EntityFunction {
      name := "getEntityByName",
      returnType := "DomainEntity",
      body := "for(e in entities) { if(e.name == name) { return e; } } return null;",
      entity := entApp
    };
// Phase 6: Model the root page as data!
// =======================================

// Create Page record for root page
var pageRoot := Page {
  name := "root",
  application := theApp
};
pageRoot.save();

// Page Elements (in order)
// ------------------------

// 0: title
var elem0 := PageElement {
  elementType := "title",
  content := "Ouroboros Metamodel",
  navigateTarget := "",
  navigateLabel := "",
  page := pageRoot,
  orderIndex := 0
};
elem0.save();

// 1: header - main
var elem1 := PageElement {
  elementType := "header",
  content := "WebDSL Modeling Itself",
  navigateTarget := "",
  navigateLabel := "",
  page := pageRoot,
  orderIndex := 1
};
elem1.save();

// 2: par - description
var elem2 := PageElement {
  elementType := "par",
  content := "Model WebDSL applications as data, then generate code.",
  navigateTarget := "",
  navigateLabel := "",
  page := pageRoot,
  orderIndex := 2
};
elem2.save();

// 3: header - Phase 1
var elem3 := PageElement {
  elementType := "header",
  content := "Phase 1: Structural",
  navigateTarget := "",
  navigateLabel := "",
  page := pageRoot,
  orderIndex := 3
};
elem3.save();

// 4-7: Phase 1 navigation links
var elem4 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "manageDomainApp",
  navigateLabel := "Manage Applications",
  page := pageRoot,
  orderIndex := 4
};
elem4.save();

var elem5 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "manageDomainEntity",
  navigateLabel := "Manage Entities",
  page := pageRoot,
  orderIndex := 5
};
elem5.save();

var elem6 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "manageEntityProperty",
  navigateLabel := "Manage Properties",
  page := pageRoot,
  orderIndex := 6
};
elem6.save();

var elem7 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "manageRelationship",
  navigateLabel := "Manage Relationships",
  page := pageRoot,
  orderIndex := 7
};
elem7.save();

// 8: header - Phase 2
var elem8 := PageElement {
  elementType := "header",
  content := "Phase 2: Behavioral",
  navigateTarget := "",
  navigateLabel := "",
  page := pageRoot,
  orderIndex := 8
};
elem8.save();

// 9-11: Phase 2 navigation links
var elem9 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "manageValidationRule",
  navigateLabel := "Manage ValidationRules",
  page := pageRoot,
  orderIndex := 9
};
elem9.save();

var elem10 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "manageDerivedProperty",
  navigateLabel := "Manage DerivedPropertys",
  page := pageRoot,
  orderIndex := 10
};
elem10.save();

var elem11 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "manageEntityFunction",
  navigateLabel := "Manage EntityFunctions",
  page := pageRoot,
  orderIndex := 11
};
elem11.save();

// 12: header - Phase 3
var elem12 := PageElement {
  elementType := "header",
  content := "Phase 3: UI/Pages",
  navigateTarget := "",
  navigateLabel := "",
  page := pageRoot,
  orderIndex := 12
};
elem12.save();

// 13-14: Phase 3 navigation links
var elem13 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "managePage",
  navigateLabel := "Manage Pages",
  page := pageRoot,
  orderIndex := 13
};
elem13.save();

var elem14 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "managePageElement",
  navigateLabel := "Manage PageElements",
  page := pageRoot,
  orderIndex := 14
};
elem14.save();

// 15: header - Actions
var elem15 := PageElement {
  elementType := "header",
  content := "Actions",
  navigateTarget := "",
  navigateLabel := "",
  page := pageRoot,
  orderIndex := 15
};
elem15.save();

// 16-17: Action navigation links
var elem16 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "codeGenerator",
  navigateLabel := "Generate Code",
  page := pageRoot,
  orderIndex := 16
};
elem16.save();

var elem17 := PageElement {
  elementType := "navigate",
  content := "",
  navigateTarget := "bootstrapPage",
  navigateLabel := "Bootstrap Metamodel (Self-Description)",
  page := pageRoot,
  orderIndex := 17
};
elem17.save();

// Total: 18 PageElements modeling the root page!
    funcAppGetEntity.save();
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
  
  // Phase 3: Generate pages from data
  var pages := theApp.pages;
  if(pages.length > 0) {
    code := code + "\n";
    for(p in pages order by p.name) {
      code := code + generatePage(p) + "\n\n";
    }
  } else {
    // Fallback: generate default root page
    code := code + "\npage root() {\n";
    code := code + "  title { \"" + theApp.name + "\" }\n";
    code := code + "  header { \"Welcome\" }\n";
    for(e in ents order by e.name) {
      code := code + "  par { navigate manage" + e.name + "() { \"Manage " + e.name + "s\" } }\n";
    }
    code := code + "}";
  }
  
  return code;
}

function generateEntity(e : DomainEntity) : String {
  var code := "entity " + e.name + " {\n";
  
  // Generate properties first
  var props := e.properties;
  for(p in props order by p.name) {
    code := code + "  " + p.name + " : " + p.propertyType + "\n";
  }
  
  // Generate derived properties
  var derivedProps := e.derivedProperties;
  for(dp in derivedProps order by dp.name) {
    code := code + "  derive " + dp.name + " : " + dp.propertyType + " = " + dp.expression + "\n";
  }
  
  // Generate relationships
  var rels := e.relationships;
  for(r in rels order by r.name) {
    code := code + generateRelationship(r);
  }
  
  // Generate validation rules
  var vals := e.validationRules;
  for(v in vals order by v.name) {
    code := code + "  validate(" + v.expression + ", \"" + v.message + "\")\n";
  }
  
  code := code + "}";
  
  // Generate entity functions as extension functions
  var funcs := e.functions;
  for(f in funcs order by f.name) {
    code := code + "\n\n" + generateEntityFunction(e, f);
  }
  
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

function generateEntityFunction(e : DomainEntity, f : EntityFunction) : String {
  var code := "extend entity " + e.name + " {\n";
  code := code + "  function " + f.name + "()";
  
  if(f.returnType != "") {
    code := code + " : " + f.returnType;
  }
  
  code := code + " {\n";
  code := code + "    " + f.body + "\n";
  code := code + "  }\n";
  code := code + "}";
  
  return code;
}

// Phase 3: Page generator
function generatePage(p : Page) : String {
  var code := "page " + p.name + "() {\n";
  
  var elems := p.elements;
  for(elem in elems order by elem.orderIndex) {
    code := code + generatePageElement(elem);
  }
  
  code := code + "}";
  return code;
}

function generatePageElement(elem : PageElement) : String {
  var code := "";
  
  if(elem.elementType == "title") {
    code := "  title { \"" + elem.content + "\" }\n";
  } else if(elem.elementType == "header") {
    code := "  header { \"" + elem.content + "\" }\n";
  } else if(elem.elementType == "par") {
    code := "  par { \"" + elem.content + "\" }\n";
  } else if(elem.elementType == "navigate") {
    code := "  par { navigate " + elem.navigateTarget + "() { \"" + elem.navigateLabel + "\" } }\n";
  }
  
  return code;
}
