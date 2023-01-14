package com.springboot.hibernate.repositories.impl;

import com.springboot.hibernate.entities.Company;
import com.springboot.hibernate.entities.Department;
import com.springboot.hibernate.repositories.IDepartmentRepository;
import com.springboot.hibernate.repositories.base.BaseRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DepartmentRepositoryImpl extends BaseRepositoryImpl<Department> implements
    IDepartmentRepository {

  @Override
  public void insertBatch(Department department, int numberOfRecords, int batchSize) {
    Department departmentInsert;
    Session session = sessionFactory.getCurrentSession();
    for (int i = 1; i <= numberOfRecords; i++) {
      departmentInsert = Department.builder()
          .departmentName(department.getDepartmentName() + " " + i)
//          .company(Company.builder().id(1L).build())
          .build();
      session.persist(departmentInsert);
      if (i % batchSize == 0 && i != numberOfRecords) {
        log.info("Flush a batch of INSERT & release memory: {} time(s)", (i / batchSize));
        session.flush();
        session.clear();
      }
    }
  }

  @Override
  public void insertNotBatch(Department department, int numberOfRecords, int batchSize) {
    Department departmentInsert;
    Session session = sessionFactory.getCurrentSession();
    outOfMem();
    List<Department> rt = new ArrayList<>();
    for (int i = 1; i <= numberOfRecords; i++) {
      departmentInsert = Department.builder()
          .departmentName(department.getDepartmentName() + " " + i)
//          .company(Company.builder().id(1L).build())
          .build();
      session.persist(departmentInsert);
      rt.add(session.load(Department.class, departmentInsert.getId()));
    }
  }

  @Override
  public void firstLevelCacheSameSession(Long id) {
    Session session = sessionFactory.getCurrentSession();
    Department department = session.load(Department.class, id);
    System.out.println(department);
    Department department1 = session.load(Department.class, id);
    System.out.println(department1);
    Department department2;
    for (int i = 0; i < 5; i++) {
      department2 = session.load(Department.class, id);
      System.out.println(department2);
    }
  }

  @Override
  public void firstLevelCacheDiffSession(Long id) {

    System.out.println("Session 1");
    Session session1 = sessionFactory.getCurrentSession();
    Department department = session1.load(Department.class, id);
    System.out.println(department);
    Department department1 = session1.load(Department.class, id);
    System.out.println(department1);
    Department department2;
    for (int i = 0; i < 5; i++) {
      department2 = session1.load(Department.class, id);
      System.out.println(department2);
    }
    System.out.println("Session 2");
    Session session2 = sessionFactory.openSession();
    Department department3 = session2.load(Department.class, id);
    System.out.println(department3);
    Department department4;
    for (int i = 0; i < 5; i++) {
      department4 = session2.load(Department.class, id);
      System.out.println(department4);
    }
  }

  private void outOfMem() {
    List<byte[]> list = new ArrayList<>();
    int index = 1;
    while (true) {
      // 1MB each loop, 1 x 1024 x 1024 = 1048576
      byte[] b = new byte[1048576];
      list.add(b);
      Runtime rt = Runtime.getRuntime();
      if (index == 2035) {
        break;
      }
      System.out.printf("[%d] free memory: %s%n", index++, rt.freeMemory());
    }
  }
}