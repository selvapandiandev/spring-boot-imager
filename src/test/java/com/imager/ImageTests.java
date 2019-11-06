package com.imager;




import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.imager.pojo.Image;

public class ImageTests {
	
	@Test
	public void imagesManagedByLombokShouldWork() {
		Image image = new Image("imageId", "imageName");
		assertThat(image.getId()).isEqualTo("imageId");
		assertThat(image.getName()).isEqualTo("imageName");
	}

}
