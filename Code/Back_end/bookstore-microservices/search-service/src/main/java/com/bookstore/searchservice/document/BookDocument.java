package com.bookstore.searchservice.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "books")
@Setting(settingPath = "elastic-settings.json")
public class BookDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String author;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String publisher;

    @Field(type = FieldType.Double)
    private BigDecimal sellingPrice;

    @Field(type = FieldType.Date)
    private LocalDate publicationDate;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String description;

    @Field(type = FieldType.Text)
    private String imageUrl;

    @Field(type = FieldType.Integer)
    private Integer pages;

    @Field(type = FieldType.Nested)
    private Set<Category> categories = new HashSet<>();

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private String id;

        @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
        private String name;

        @Field(type = FieldType.Text)
        private String description;
    }
}