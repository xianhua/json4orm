# Json4orm
## Motivation
Make data in relational database searchable online and deliverable with customizable format, without programming.  
## Main Features
- Support defining database object-relational mappings (ORMs) in JSON
- Support defining database queries in JSON with customized fields to return as result
- Support database query engine to execute queries defined in JSON based on ORMs defined in JSON
## Quick Example
### Entity Mapping
Student
```
#student.json
{
  name: "Student",
  table: "student",
  properties: [
    {
      name: "studentId",
      type: "ID",
      column: "student_id"
    },
    {
      name: "firstName",
      type: "string",
      column: "first_name"
    },
    {
      name: "lastName",
      type: "string",
      column: "last_name"
    },
    {
      name: "middleName",
      type: "string",
      column: "middle_name"
    },
    {
      name: "birthDate",
      type: "date",
      column: "birth_date"
    },
    {
      name: "createdAt",
      type: "timestamp",
      column: "created_at"
    }
  ]
}
```
Class
```
{
  name: "Class",
  table: "class",
  properties: [
    {
      name: "classId",
      type: "ID",
      column: "class_id"
    },
    {
      name: "name",
      type: "string",
      column: "name"
    },
    {
      name: "createdAt",
      type: "timestamp",
      column: "created_at"
    },
    {
      name: "classStudents",
      type: "list",
      column: "class_id",
      itemType: "ClassStudent"
    }
  ]
}
```
ClassStudent
```
{
  name: "ClassStudent",
  table: "class_student",
  properties: [
    {
      name: "classStudentId",
      type: "ID",
      column: "class_student_id"
    },
    {
      name: "class",
      type: "Class",
      column: "class_id"
    },
    {
      name: "student",
      type: "Student",
      column: "student_id"
    },
    {
      name: "score",
      type: "float",
      column: "score"
    },
    {
      name: "createdAt",
      type: "timestamp",
      column: "created_at"
    }
  ]
}
```
### Query in Json
```
{
  queryFor: "Student",
  filter: {
    and: [
      {
         firstName: "John"
      },
      {
        "classStudents.class.name": {
           in: ["Math","English","History"]
        } 
      }
    ]   
  },
  result: {
    Student: {
      properties: ["firstName", "lastName","birthDate"],
      classStudents: {
        properties: ["classStudentId", "score"],
        class: ["name"]
      }
    }  
  }
}
```
## User Guide and API Guide
Please visit http://json4orm.com for user guide and API guide.
