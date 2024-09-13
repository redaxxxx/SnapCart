package com.android.developer.prof.reda.snapcart.util
fun validationFullName(fullName: String): AddressValidation {
    if (fullName.isEmpty()){
        return AddressValidation.Failed("Full Name cannot be empty")
    }
    return AddressValidation.Success
}
fun validationAddress(address: String): AddressValidation{
    if (address.isEmpty()){
        return AddressValidation.Failed("Address cannot be empty")
    }
    return AddressValidation.Success
}
fun validationState(state: String): AddressValidation{
    if (state.isEmpty()){
        return AddressValidation.Failed("state cannot be empty")
    }
    return AddressValidation.Success
}
fun validationCity(city: String): AddressValidation{
    if (city.isEmpty()){
        return AddressValidation.Failed("city cannot be empty")
    }
    return AddressValidation.Success
}
fun validationZipCode(zipCode: String): AddressValidation{
    if (zipCode.isEmpty()){
        return AddressValidation.Failed("Zip Code cannot be empty")
    }
    return AddressValidation.Success
}
fun validationCountry(country: String): AddressValidation{
    if (country.isEmpty()){
        return AddressValidation.Failed("country cannot be empty")
    }
    return AddressValidation.Success
}