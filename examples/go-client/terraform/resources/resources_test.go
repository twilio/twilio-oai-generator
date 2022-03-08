package openapi

import (
	"io/ioutil"
	"strings"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestResourceCount(t *testing.T) {
	resourceFile, _ := ioutil.ReadFile("../resources/api_default.go")
	assert.Equal(t, 3, strings.Count(string(resourceFile), "func Resource"))
}
