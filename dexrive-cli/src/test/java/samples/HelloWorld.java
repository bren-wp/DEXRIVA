package samples;

/**
 * Compiled by Gradle's compileTestJava task into samples/HelloWorld.class.
 * CLI integration tests use the resulting class file as a decompilation fixture.
 *
 * Keeping this fixture as Java source avoids committing ignored .class binaries
 * and makes the tests reproducible on clean CI checkouts.
 */
public class HelloWorld {
	public static void main(String[] args) {
		System.out.println("Hello, world!");
	}
}
