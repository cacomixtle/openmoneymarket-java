package finance.omm.commons;

public class ArrayAddress {

	public ArrayAddress() {}
	/*
	public ArrayAddress(List<AddressDetails> addressDetails) {
		this.addressDetails = addressDetails;
	}
	private List<AddressDetails> addressDetails;
	public List<AddressDetails> getAddressDetails() {
		return addressDetails;
	}
	public void setAddressDetails(List<AddressDetails> addressDetails) {
		this.addressDetails = addressDetails;
	}*/

	
	private AddressDetails[] addressDetails;
	
	public ArrayAddress(AddressDetails[] addressDetails) {
		this.addressDetails = addressDetails;
	}
	

	public AddressDetails[] getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(AddressDetails[] addressDetails) {
		this.addressDetails = addressDetails;
	}

}
