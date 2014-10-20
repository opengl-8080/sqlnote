CREATE TABLE SQL_NOTE (
    ID INTEGER NOT NULL IDENTITY,
    TITLE VARCHAR(128) NOT NULL,
    SQL_TEMPLATE LONGVARCHAR
);

CREATE TABLE SQL_PARAMETERS (
    SQL_ID INTEGER NOT NULL,
    NAME VARCHAR(128),
    SORT_ORDER INTEGER NOT NULL,
    CONSTRAINT SQL_PARAMETERS_FK1
      FOREIGN KEY (SQL_ID) REFERENCES SQL_NOTE (ID)
);

/*
CREATE TABLE CATEGORY_NODE (
    ID INTEGER NOT NULL IDENTITY,
    TITLE VARCHAR(128) NOT NULL
);

CREATE TABLE TREE_STRUCTURE (
    ID INTEGER NOT NULL IDENTITY,
    PARENT_NODE_ID INTEGER,
    IS_CATEGORY BOOLEAN NOT NULL,
    CATEGORY_ID INTEGER,
    SQL_ID INTEGER,
    CONSTRAINT TREE_STRUCTURE_FK1
      FOREIGN KEY (PARENT_NODE_ID)
      REFERENCES TREE_STRUCTURE (ID),
      
    CONSTRAINT TREE_STRUCTURE_FK2
      FOREIGN KEY (CATEGORY_ID)
      REFERENCES CATEGORY_NODE (ID),
      
    CONSTRAINT TREE_STRUCTURE_FK3
      FOREIGN KEY (SQL_ID)
      REFERENCES SQL_NOTE (ID)
);
*/
