package unit

import (
	"io/ioutil"
	"strings"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestSingleResource(t *testing.T) {
	resourceFile, _ := ioutil.ReadFile("../resources/api_default.go")
	assert.Equal(t, 1, strings.Count(string(resourceFile), "func Resource"))
}
