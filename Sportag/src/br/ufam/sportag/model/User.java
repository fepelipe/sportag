package br.ufam.sportag.model;

public class User {
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getPhotoPrefix() {
		return photoPrefix;
	}

	public void setPhotoPrefix(String photoPrefix) {
		this.photoPrefix = photoPrefix;
	}

	public String getPhotoSuffix() {
		return photoSuffix;
	}

	public void setPhotoSuffix(String photoSuffix) {
		this.photoSuffix = photoSuffix;
	}

	public String toString() {
        return String.format("%s, %s", firstName, lastName);
    }
	
	public int id;
	public String firstName;
	public String lastName;
	public String gender;
	public String relationship;
	public String homeCity;
	public String bio;
	public String photoPrefix;
	public String photoSuffix;

}


