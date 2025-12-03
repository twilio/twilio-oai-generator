"""
OneOf Example - Demonstrates how to use oneOf functionality in Twilio Python SDK

This example shows how to work with APIs that accept multiple different object types
for the same parameter using the oneOf pattern.
"""

from twilio.rest import Client
import os

# Initialize the Twilio client
# Replace these with your actual credentials or set them as environment variables
account_sid = os.environ.get('TWILIO_ACCOUNT_SID', 'your_account_sid')
auth_token = os.environ.get('TWILIO_AUTH_TOKEN', 'your_auth_token')

client = Client(account_sid, auth_token)


def example_1_create_cat():
    """
    Example 1: Create a pet using the Cat schema

    The Cat schema includes properties like account_sid, param1, and param2.
    """
    print("\n=== Example 1: Creating a Pet with Cat Schema ===")

    # Create a Cat object with Cat-specific properties
    cat = client.one_of.v1.pets.Cat({
        "account_sid": "AC1234567890abcdef",
        "param1": "Persian",
        "param2": "White",
    })

    # Inspect the cat object before sending
    cat_dict = cat.to_dict()
    print(f"Cat object: {cat_dict}")

    # Create the pet using the Cat schema
    try:
        pet = client.one_of.v1.pets.create(cat=cat)
        print(f"✓ Successfully created pet with account_sid: {pet.account_sid}")
        print(f"  Param1: {pet.param1}")
        print(f"  Param2: {pet.param2}")
    except Exception as e:
        print(f"✗ Error creating pet: {e}")


def example_2_create_dog():
    """
    Example 2: Create a pet using the Dog schema

    The Dog schema includes different properties like type, name, and pack_size.
    """
    print("\n=== Example 2: Creating a Pet with Dog Schema ===")

    # Create a Dog object with Dog-specific properties
    dog = client.one_of.v1.pets.Dog({
        "type": "dog",
        "name": "Bruno",
        "pack_size": 5
    })

    # Inspect the dog object
    dog_dict = dog.to_dict()
    print(f"Dog object: {dog_dict}")

    # Note: Currently the generated code accepts 'cat' parameter
    # This demonstrates the object creation and serialization
    print("✓ Dog object created and serialized successfully")


def example_3_nested_oneof():
    """
    Example 3: Working with nested oneOf objects

    This shows how a Cat can contain a Dog object (nested oneOf).
    """
    print("\n=== Example 3: Nested OneOf Objects ===")

    # Create a nested Dog object
    nested_dog = client.one_of.v1.pets.Dog({
        "type": "dog",
        "name": "Rex",
        "pack_size": 3
    })

    # Create a Cat that contains the Dog
    cat_with_dog = client.one_of.v1.pets.Cat({
        "account_sid": "AC1234567890abcdef",
        "param1": "Siamese",
        "param2": "Brown",
        "dog": nested_dog,  # Nested oneOf object
        "object1": "extra_data_1",
        "object2": "extra_data_2"
    })

    # Inspect the nested structure
    cat_dict = cat_with_dog.to_dict()
    print(f"Cat with nested Dog:")
    print(f"  account_sid: {cat_dict['account_sid']}")
    print(f"  param1: {cat_dict['param1']}")
    print(f"  param2: {cat_dict['param2']}")
    print(f"  nested dog: {cat_dict['dog']}")
    print(f"  object1: {cat_dict['object1']}")
    print(f"  object2: {cat_dict['object2']}")

    try:
        pet = client.one_of.v1.pets.create(cat=cat_with_dog)
        print("✓ Successfully created pet with nested oneOf object")
    except Exception as e:
        print(f"✗ Error creating pet: {e}")


def example_4_minimal_fields():
    """
    Example 4: Creating objects with minimal required fields

    OneOf objects support optional fields - you only need to provide the fields you need.
    """
    print("\n=== Example 4: Minimal Fields ===")

    # Create a Cat with minimal fields
    minimal_cat = client.one_of.v1.pets.Cat({
        "account_sid": "AC1234567890abcdef"
    })

    cat_dict = minimal_cat.to_dict()
    print(f"Cat with minimal fields:")
    print(f"  account_sid: {cat_dict['account_sid']}")
    print(f"  param1: {cat_dict['param1']}")  # Will be None
    print(f"  param2: {cat_dict['param2']}")  # Will be None
    print("✓ Minimal Cat object created successfully")


def example_5_dynamic_creation():
    """
    Example 5: Dynamic object creation based on type

    This shows a practical pattern for creating different types dynamically.
    """
    print("\n=== Example 5: Dynamic Object Creation ===")

    def create_animal(animal_type, **properties):
        """Helper function to create animals dynamically"""
        if animal_type == 'cat':
            return client.one_of.v1.pets.Cat(properties)
        elif animal_type == 'dog':
            return client.one_of.v1.pets.Dog(properties)
        else:
            raise ValueError(f"Unknown animal type: {animal_type}")

    # Create different animals dynamically
    animals = [
        ('cat', {"account_sid": "AC111", "param1": "Maine Coon", "param2": "Large"}),
        ('dog', {"type": "dog", "name": "Max", "pack_size": 8}),
        ('cat', {"account_sid": "AC222", "param1": "Tabby", "param2": "Orange"}),
    ]

    for animal_type, properties in animals:
        animal = create_animal(animal_type, **properties)
        animal_dict = animal.to_dict()
        print(f"✓ Created {animal_type}: {animal_dict}")


def example_6_error_handling():
    """
    Example 6: Error handling with oneOf objects
    """
    print("\n=== Example 6: Error Handling ===")

    try:
        # Attempt to create a pet
        cat = client.one_of.v1.pets.Cat({
            "account_sid": "AC_TEST",
            "param1": "test_value"
        })

        # This would make an actual API call
        # pet = client.one_of.v1.pets.create(cat=cat)

        print("✓ Object created successfully (API call not made in this example)")

    except Exception as e:
        print(f"✗ Error occurred: {type(e).__name__}: {e}")


async def example_7_async_usage():
    """
    Example 7: Async usage of oneOf functionality

    This example requires async context to run.
    """
    print("\n=== Example 7: Async Usage ===")

    from twilio.http.async_http_client import AsyncTwilioHttpClient

    # Create async client
    http_client = AsyncTwilioHttpClient()
    async_client = Client(account_sid, auth_token, http_client=http_client)

    try:
        # Create a Cat object
        cat = async_client.one_of.v1.pets.Cat({
            "account_sid": "AC1234567890abcdef",
            "param1": "Russian Blue",
            "param2": "Gray"
        })

        # This would make an async API call
        # pet = await async_client.one_of.v1.pets.create_async(cat=cat)

        print("✓ Async object created successfully (API call not made in this example)")

    finally:
        # Clean up async resources
        await http_client.session.close()


def main():
    """
    Run all examples
    """
    print("=" * 60)
    print("Twilio Python SDK - OneOf Functionality Examples")
    print("=" * 60)

    # Run synchronous examples
    example_1_create_cat()
    example_2_create_dog()
    example_3_nested_oneof()
    example_4_minimal_fields()
    example_5_dynamic_creation()
    example_6_error_handling()

    # Async example requires event loop
    print("\n=== Note on Async Example ===")
    print("To run the async example, use:")
    print("  import asyncio")
    print("  asyncio.run(example_7_async_usage())")

    print("\n" + "=" * 60)
    print("Examples completed!")
    print("=" * 60)


if __name__ == "__main__":
    main()
