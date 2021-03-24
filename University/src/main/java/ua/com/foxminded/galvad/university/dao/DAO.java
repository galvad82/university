package ua.com.foxminded.galvad.university.dao;

import java.util.List;

public interface DAO<K extends Number, T> {
	
	public void create(T entity);
	
	public T retrieve(K id);
	
	public void update(T entity);

	public void delete(K id);

	public void delete(T entity);
	
	public List<T> findAll();

}