package openapi

import (
	"strings"
	"testing"

	"os"

	"github.com/stretchr/testify/assert"
)

func TestResourceCount(t *testing.T) {
	resourceFile, _ := os.ReadFile("api_default.go")
	assert.Equal(t, 3, strings.Count(string(resourceFile), "func Resource"))
}
