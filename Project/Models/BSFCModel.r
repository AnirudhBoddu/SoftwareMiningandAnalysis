########## Loading required library
library(readr) 
########## Loading the dataset with exception flow metrics
data <- read_csv("merged_exceptionFlowMetrics.csv") 
###### Dropping categorical data
drop=c("Filename","ExceptionType","ParentMethod","ParentType","ThrownType", "CaughtType","DeclaringMethod","InvokedMethod")
data=data[,!(names(data) %in% drop)]
### Splitting 2/3rd of the data for training and 1/3rd of the data for testing
training_size= round(2*length(data$numberOfAttributes)/3,digits = 0)
testing_size=length(data$numberOfAttributes)-training_size
### Randomly giving index numbers
training_index=sample(nrow(data), training_size)
### Computing training and testing datsets
trainingset=data[training_index, ]
testset=data[-training_index, ]
### Creating an independant datset without post_release_bugs column
drop2=c("post_release_bugs")
independant=trainingset[,!(names(trainingset) %in% drop2)]
### Correlation analysis
correlations <- cor(independant, method="spearman")
### Finding highly correlated columns
highCorr <- findCorrelation(correlations, cutoff = .75)
### Computing a dataset with non highly correlated columns
low_cor_names=names(independant[, -highCorr])
low_cor_data= independant[(names(independant) %in% low_cor_names)]
### Performing redundancy analysis
redun_obj = redun (~. ,data = dataforredun ,nk =0)
after_redun= dataforredun[,!(names(dataforredun) %in% redun_obj $Out)]
### Creating formula object for the model
form=as.formula(paste("post_release_bugs>0~",paste(names(after_redun),collapse="+")))
form
### Building a GLM model for the data using the above formula object
model=glm(formula=form, data=log10(trainingset+1), family = binomial(link = "logit"))
summary(model)
### How well is the model performing in terms of precision and recall
predictions <- predict(model, log10(testset+1) ,type="response")
TP = sum((predictions>0.5) & (testset$post_release_bugs>0))
precision = TP / sum((predictions>0.5))
recall = TP / sum(testset$post_release_bugs>0)
precision
recall