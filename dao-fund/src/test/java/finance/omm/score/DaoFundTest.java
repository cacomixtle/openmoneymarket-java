package finance.omm.score;

import static java.math.BigInteger.TEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.iconloop.score.test.Account;
import com.iconloop.score.test.Score;
import com.iconloop.score.test.ServiceManager;
import com.iconloop.score.test.TestBase;

import finance.omm.commons.AddressDetails;
import finance.omm.commons.Addresses;
import score.Address;

class DaoFundTest extends TestBase{

	private static ServiceManager sm = getServiceManager();
	private static Account owner = sm.createAccount();

	private BigInteger decimals = BigInteger.valueOf(10);
	private static BigInteger totalSupply = BigInteger.valueOf(50000000000L);

	private Score daoFund;
	private Score ommToken;
	private Account accountAddressProvider = sm.createAccount();
	private Account accountGovernance = sm.createAccount();
	private AddressDetails governanceDetails = new AddressDetails( Addresses.GOVERNANCE, accountGovernance.getAddress());
	private AddressDetails ommTokenDetails;

	@BeforeAll
	public static void init() {
		owner.addBalance(DaoFund.OMM_TOKEN, totalSupply);
	}

	@BeforeEach
	public void setup() throws Exception {
		daoFund = sm.deploy(owner, DaoFund.class, accountAddressProvider.getAddress(), false);
		ommToken = sm.deploy(owner, TestOmmToken.class);
		ommTokenDetails = new AddressDetails( Addresses.OMM_TOKEN, ommToken.getAddress());
		ommToken.getAccount().addBalance(DaoFund.OMM_TOKEN, totalSupply);
	}

	@Test
	void testSetAddresses() {

		AddressDetails[] addressDetails = {governanceDetails, ommTokenDetails};
		daoFund.invoke(accountAddressProvider, "setAddresses", (Object)addressDetails);

		@SuppressWarnings("unchecked")
		Map<String, Address> addresses = (Map<String, Address>)daoFund.call("getAddresses");

		assertNotNull(addresses);
		assertFalse(addresses.isEmpty());
		assertNotNull(addresses.get(governanceDetails.getName()));
	}

	@Test
	void testTransferOmm() {

		testSetAddresses();

		BigInteger amount = TEN.pow(decimals.intValue());
		Account receiver = sm.createAccount();

		ommToken.invoke(owner, "setOwner", ommToken.getAccount());
		ommToken.invoke(owner, "addTo", receiver);
		daoFund.invoke(accountGovernance, "transferOmm", amount, receiver.getAddress());

		assertEquals(amount, receiver.getBalance(DaoFund.OMM_TOKEN));
		assertEquals(totalSupply.subtract(amount), ommToken.getAccount().getBalance(DaoFund.OMM_TOKEN));

	}
}
