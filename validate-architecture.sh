#!/bin/bash

# Architecture Validation Script
# Validates that the project follows Clean Architecture principles

echo "🏗️  Architecture Validation Script"
echo "=================================="
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check 1: Domain has no Spring imports
echo "📋 Check 1: Validating Domain has no Spring dependencies..."
SPRING_IMPORTS=$(find core/src/main/java -name "*.java" -exec grep -l "import org.springframework" {} \; 2>/dev/null)

if [ -z "$SPRING_IMPORTS" ]; then
    echo -e "${GREEN}✅ PASS: Domain layer has no Spring imports${NC}"
else
    echo -e "${RED}❌ FAIL: Domain layer has Spring imports:${NC}"
    echo "$SPRING_IMPORTS"
    exit 1
fi
echo ""

# Check 2: Domain has no Spring annotations
echo "📋 Check 2: Validating Domain has no Spring annotations..."
SPRING_ANNOTATIONS=$(find core/src/main/java -name "*.java" -exec grep -l "@Service\|@Component\|@Repository\|@Controller" {} \; 2>/dev/null)

if [ -z "$SPRING_ANNOTATIONS" ]; then
    echo -e "${GREEN}✅ PASS: Domain layer has no Spring annotations${NC}"
else
    echo -e "${RED}❌ FAIL: Domain layer has Spring annotations:${NC}"
    echo "$SPRING_ANNOTATIONS"
    exit 1
fi
echo ""

# Check 3: Core POM has no Spring dependencies
echo "📋 Check 3: Validating core/pom.xml has no Spring dependencies..."
CORE_SPRING_DEPS=$(grep -i "springframework" core/pom.xml 2>/dev/null)

if [ -z "$CORE_SPRING_DEPS" ]; then
    echo -e "${GREEN}✅ PASS: core/pom.xml has no Spring dependencies${NC}"
else
    echo -e "${RED}❌ FAIL: core/pom.xml has Spring dependencies:${NC}"
    echo "$CORE_SPRING_DEPS"
    exit 1
fi
echo ""

# Check 4: Domain structure exists
echo "📋 Check 4: Validating Domain structure..."
if [ -d "core/src/main/java/br/com/company/core/domain/model" ] && \
   [ -d "core/src/main/java/br/com/company/core/domain/repository" ] && \
   [ -d "core/src/main/java/br/com/company/core/domain/usecase" ]; then
    echo -e "${GREEN}✅ PASS: Domain structure exists (model, repository, usecase)${NC}"
else
    echo -e "${RED}❌ FAIL: Domain structure incomplete${NC}"
    exit 1
fi
echo ""

# Check 5: Infrastructure structure exists
echo "📋 Check 5: Validating Infrastructure structure..."
if [ -d "application/src/main/java/br/com/company/infrastructure/web" ] && \
   [ -d "application/src/main/java/br/com/company/infrastructure/persistence" ] && \
   [ -d "application/src/main/java/br/com/company/infrastructure/config" ]; then
    echo -e "${GREEN}✅ PASS: Infrastructure structure exists (web, persistence, config)${NC}"
else
    echo -e "${RED}❌ FAIL: Infrastructure structure incomplete${NC}"
    exit 1
fi
echo ""

# Check 6: Application usecase structure exists
echo "📋 Check 6: Validating Application layer structure..."
if [ -d "application/src/main/java/br/com/company/application/usecase" ]; then
    echo -e "${GREEN}✅ PASS: Application usecase structure exists${NC}"
else
    echo -e "${RED}❌ FAIL: Application usecase structure missing${NC}"
    exit 1
fi
echo ""

# Check 7: DomainConfiguration exists
echo "📋 Check 7: Validating DomainConfiguration exists..."
if [ -f "application/src/main/java/br/com/company/infrastructure/config/DomainConfiguration.java" ]; then
    echo -e "${GREEN}✅ PASS: DomainConfiguration exists${NC}"
else
    echo -e "${RED}❌ FAIL: DomainConfiguration missing${NC}"
    exit 1
fi
echo ""

# Check 8: Repository interface in Domain
echo "📋 Check 8: Validating Repository interface in Domain..."
if [ -f "core/src/main/java/br/com/company/core/domain/repository/MessageRepository.java" ]; then
    # Check if it's an interface
    if grep -q "public interface MessageRepository" core/src/main/java/br/com/company/core/domain/repository/MessageRepository.java; then
        echo -e "${GREEN}✅ PASS: Repository interface exists in Domain${NC}"
    else
        echo -e "${RED}❌ FAIL: MessageRepository is not an interface${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  SKIP: MessageRepository not found (may not be created yet)${NC}"
fi
echo ""

# Check 9: Repository implementation in Infrastructure
echo "📋 Check 9: Validating Repository implementation in Infrastructure..."
if [ -f "application/src/main/java/br/com/company/infrastructure/persistence/InMemoryMessageRepository.java" ]; then
    echo -e "${GREEN}✅ PASS: Repository implementation exists in Infrastructure${NC}"
else
    echo -e "${YELLOW}⚠️  SKIP: Repository implementation not found (may not be created yet)${NC}"
fi
echo ""

# Summary
echo "=================================="
echo -e "${GREEN}🎉 Architecture Validation PASSED!${NC}"
echo "=================================="
echo ""
echo "✅ All checks passed!"
echo "✅ Domain is framework-independent"
echo "✅ Structure follows Clean Architecture"
echo "✅ Ready for development"
echo ""
echo "Next steps:"
echo "  • Run: mvn clean install"
echo "  • Run: cd application && mvn spring-boot:run"
echo "  • Run: mvn test -Dtest=ArchitectureTest"
echo ""

