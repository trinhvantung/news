package vn.trinhtung.model;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ReviewStarCount {
	private Byte star;
	private Long quantity;
	private Integer percent;

	public ReviewStarCount(Byte star, Long quantity) {
		super();
		this.star = star;
		this.quantity = quantity;
	}

	public ReviewStarCount(Byte star) {
		super();
		this.star = star;
	}

	@Override
	public int hashCode() {
		return Objects.hash(star);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReviewStarCount other = (ReviewStarCount) obj;
		return Objects.equals(star, other.star);
	}

	public ReviewStarCount(Byte star, Long quantity, Integer percent) {
		super();
		this.star = star;
		this.quantity = quantity;
		this.percent = percent;
	}

}
