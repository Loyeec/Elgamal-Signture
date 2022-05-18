import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
 
public class ElGamal {
	static BigInteger p, g; // 大素数和本原元
	static BigInteger C, C1;// 密文
 
	 
 
	public static void main(String[] args) {
		BigInteger y, x; // 随机数 P,g是P的生成元，公钥<y,g,p>，私钥<x,g,p> 0<a<p
		int z=32;
		//p=BigInteger.valueOf(7);
		//g=BigInteger.valueOf(5);
 
	 
		while (true) {
			System.out.println("请输入明文:");
			Scanner input = new Scanner(System.in);
			String message = input.nextLine();//明文
			int hm = message.hashCode();//明文的哈希值
			System.out.println("该明文的哈希值为："+hm);
 
			/*int hm = message.hashCode();//明文的哈希值
			System.out.println("该明文的哈希值为："+hm);*/
 
			ElGamal.getRandomP(z);
			x = ElGamal.getRandoma(p);
			y = ElGamal.calculatey(x, g, p);
			System.out.println("计算机随机生成的素数P:" + p);
			System.out.println("求得其生成元:" + g);
			System.out.println("私钥<x,g,p>为: (" + x + "," + g + "," + p + ")");
			System.out.println("公钥<y,g,p>为:" + "(" + y + "," + g + "," + p
					+ ")");
			
			ElGamal.encrypt(hm, x, p, g);
			ElGamal.decrypt(C, C1, hm,g, p,y);
			
 
			//System.out.println("加密后的密文为:" + C + "," + C1);
 
//			String designm = ElGamal.decrypt(C, C1, hm,g, p,y);
			//System.out.println("解密得到明文为:" + designm);
		}
	}
 
	/** 取一个大的随机素数P,和P的生成元g */
	public static void getRandomP(int z) {
		Random r = new Random();
		BigInteger q = null;
		while (true) {
			q = BigInteger.probablePrime(z, r);
			if (q.bitLength() !=z)
				continue;
			if (q.isProbablePrime(10)) // 如果q为素数则再进一步计算生成元
			{
				p = q.multiply(new BigInteger("2")).add(BigInteger.ONE);
				if (p.isProbablePrime(10)) // 如果P为素数则OK~，否则继续
					break;
			}
		}
		while (true) {
			g = BigInteger.probablePrime(p.bitLength() - 1, r);// 产生一0<=k<p-1的随机素数
			if (!g.modPow(BigInteger.ONE, p).equals(BigInteger.ONE)
					&& !g.modPow(q, p).equals(BigInteger.ONE)) {
				break;
			}
		}
	}
 
	/** 取随机数a */
	public static BigInteger getRandoma(BigInteger p) {
		BigInteger a;
		Random r = new Random();
		a = new BigInteger(p.bitLength() - 1, r);// 产生一0<=a<p-1的随机数
		System.out.println("随机数 x 为:" + a);
		return a;
	}
 
	/** 计算密钥的值 */
	public static BigInteger calculatey(BigInteger x, BigInteger g, BigInteger p) {
		BigInteger y;
		y = g.modPow(x, p);//y=g^a mod p
		return y;
	}
 
	/** 签名 */
	public static void encrypt(int m, BigInteger x, BigInteger p,
			BigInteger g) {
//		BigInteger message = new BigInteger(m.getBytes());// 把字串转成一个BigInteger对象
		BigInteger message=BigInteger.valueOf(m);
		Random r = new Random();
		BigInteger k;
		while (true) {
			k = new BigInteger(p.bitLength() - 2, r);// 产生一0<=k<p-2的随机数
			if (k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {// 如果随机数与p-1互质																			// 则选取成功,返回随机数k
				System.out.println("随机数 k 为:"+k);
				break;
			}
		}
		// 计算签名C,C1
		C = g.modPow(k, p);//c=g^k mod p
		BigInteger p_MinusOne=p.subtract(BigInteger.ONE);
		BigInteger k_Reverse = k.modInverse(p_MinusOne);
        System.out.println("k对p-1的逆元 = " + k_Reverse.toString());
		C1=k_Reverse.multiply(message.subtract(x.multiply(C))).mod(p_MinusOne);
		System.out.println("签名C:"+C);
		System.out.println("签名C1:"+C1);
		
	}
 
	/** 认证 */
	public static void decrypt(BigInteger C, BigInteger C1, int hm,BigInteger g,
			BigInteger p,BigInteger y) {
		
		BigInteger m=BigInteger.valueOf(hm);
		BigInteger v1,v2;
		
		v1=g.modPow(m,p);
		v2=((y.modPow(C,p)).multiply(C.modPow(C1,p))).mod(p);
		System.out.println("认证v1:"+v1);
		System.out.println("认证v2:"+v2);
 
	}
 
	
}