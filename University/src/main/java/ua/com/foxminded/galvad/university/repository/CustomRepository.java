package ua.com.foxminded.galvad.university.repository;

public interface CustomRepository<S,T> {
		
	T getID(S s);

}
