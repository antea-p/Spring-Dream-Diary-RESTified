package ac.rs.metropolitan.anteaprimorac5157.service;

public enum DiaryEntrySortingCriteria {
   TITLE, CREATEDDATE;

   @Override
   public String toString() {
     return (this == TITLE) ? "title" : "createdDate";
   }
}
