package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.MstEmployee;

@Repository
public interface EmployeeRepository extends JpaRepository<MstEmployee, Integer>{

	Optional<MstEmployee> findByEmpCode(int empCode);
	

	  // Query to fetch employees by location code
	@Query("SELECT emp FROM MstEmployee emp WHERE emp.locCode = :loc AND emp.empStatus = 'Active'")
	List<MstEmployee> findEmployeesByLocationCode(@Param("loc") String loc);

    // Query to get employee name by employee code (IOCL)
	@Query("SELECT e.empName FROM MstEmployee e WHERE e.empCode = :empCode")
    String getEmployeeNameByEmpCode(int empCode);


	
	@Query(value = "SELECT emp_name FROM emp_db WHERE loc_name = :locName AND psa = :psa AND emp_status = 'Active'", nativeQuery = true)
	List<String> findEmployeeNamesByLocNameAndPsaAndStatus(@Param("locName") String locName, @Param("psa") String psa);
	
    @Query("SELECT e.empCode FROM MstEmployee e")
    List<Integer> findAllEmployeeCodes();
    
    @Query("SELECT e.empCode FROM MstEmployee e WHERE e.locCode = :locCode")
    List<String> findEmpCodesByLocCode(@Param("locCode") String locCode);
    
    @Query("SELECT e.empName FROM MstEmployee e WHERE e.empCode = :empCode")
    Optional<String> findEmpNameByEmpCode(@Param("empCode") Integer empCode);
    
    
	
    @Query(value = "SELECT * FROM emp_db WHERE loc_code = :loc AND psa = :dept AND emp_status = 'Active'", nativeQuery = true)
    List<MstEmployee> findEmployeeNamesByLocCodeAndPsaAndStatus(@Param("loc") String loc, @Param("dept") String dept);

    
	@Query(value = "SELECT * FROM emp_db WHERE loc_code = :locCode", nativeQuery = true)
    List<MstEmployee> findByLocCode(@Param("locCode") String locCode);
	
    Optional<MstEmployee> findByLocCodeAndEmpName(String locCode, String empName);
    
    @Query("SELECT e.empName FROM MstEmployee e")
    List<String> findAllEmpNames();

    @Query("SELECT e.empName FROM MstEmployee e WHERE e.locName = :loc")
    List<String> findEmployeeNamesByLocName(@Param("loc") String loc);



}
