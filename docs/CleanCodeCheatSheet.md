# Clean Code Cheat Sheet

### Why? - Technical Debt
- is the cost of postponing proper coding practices and having much more work later for bug fixes and new features

![Technical Debt](https://accesto.com/blog/static/02e67d8e9086938238bb38ecff16b48e/fd371/debt-vs-productivity.jpg)<br>
*Technical Debt Visualization [Source](https://accesto.com/blog/static/02e67d8e9086938238bb38ecff16b48e/fd371/debt-vs-productivity.jpg)*

### General Good Coding Practices
#### **Dead Code**
- unused code is code that is collecting dust
- adds confusion and should be promptly removed or a taged like TODO or relocated into a new feature branch

#### **Comments**
- should focus on explaining *why* rather than *what*
- good API design is more valuable than comments explaining poor design choices
- comments do not justify bad and unreadable code -> dont add a comment refactor the code

#### **Variables and Naming**
- choose clear and concise names for variables, functions and classes
- describe methods with verbs, classes with types, and interfaces with functionality
- names should often be discussed with other programmers to find a name that is liked and understood by everyone
- declare variables close to their usage to improve code readability
```python
# Bad
def convert_minutes_to_seconds(x):
    value = x * 60
    ...

# Better
def convert_minutes_to_seconds(minutes):
    seconds = minutes * 60
    ...
```

#### **Magic Numbers**
- replace magic numbers with named constants for clarity and maintainability
- define variables for temporary values instead of using raw numbers
```python
# Unclear
total = price * 0.19

# Clear
TAX = 0.19
total = price * TAX
```

#### **Polymorphism**
- polymorphism is like having tools that adapt to different tasks 
- utilize polymorphism to enhance code readability and maintainability
- prefer polymorphic structures over extensive if-else/switch-case chains

#### **Changes**
- changes should have minimal impact, ideally limited to local consequences
- avoid side effects that affect distant parts of the codebase
- sometimes its not possible to avoid side effects, then use comments

#### **Fields and State**
- class fields are like the drawers in your desk -  keep them organized with only what's necessary. Don't store temporary stuff where it doesn't belong
- class fields should represent strict object states, not temporary data
- separate concerns by assigning roles to classes and keeping their states clean

#### **Exception Handling and Edge Cases**
- plan exceptions and edge cases during code development, not in production 👀
- handle exceptions where they occur and provide meaningful error messages

#### **Avoid Nesting**
- be a never nester
- break down large statements into smaller functions
- utilize assertions for pre and post conditions
- encapsulate complex conditions into readable methods

```python
# Bad
def process_data(data):
    if data:
        for item in data:
            if item.is_valid():
                if item.should_process():
                    result = item.calculate()
                    print(result)
# Better
def process_data(data):
    if not data:
        return
    
    for item in data:
        if not item.is_valid() or not item.should_process():
            continue

        result = item.calculate()
        print(result)
```


### Coding Principles

#### **Grade 1 Red**
- DRY - encapsulate commonly used code into functions or classes
- KISS - code should be simple, flexible, and easy readable
- Avoid Premature Optimization - prioritize readability and standardization over not necessary optimizations

#### **Grade 2 Orange**
- Separation of Concerns - separate different domains, such as logging, tracing, caching, controller, and business logic 
- isolate concerns to improve code maintainability
- Single Responsibility - functions and classes should specialize on only one task and not be allrounders

#### **Grade 3 Yellow**
- Dependency Inversion - high-level classes should not depend excessively on low-level classes to enable independent testing
- Interface Segregation - a client should depend as little as possible on other dependencies
- Information Hiding - expose only necessary functions to other parts of the code, keep as much as possible private

#### **Grade 4 Green**
- Law of Demeter - a class should only interact with its nearest dependencies and not with the internal details of objects it manipulates indirectly
- Tell, Don't Ask - whenever possible tell objects what to do, don't ask them about their state

#### **Grade 5 Blue**
- You Ain't Gonna Need It - only implement what you need now, not what you anticipate needing in the future, maybe create new tasks in the backlog but don't start implementing

### IMPORTANT - KEEP IN MIND
- follow these guidelines to prevent technical debt
- maintain consistent coding standards within a project
- leave every place better than you found it
- prioritize root cause analysis over treating symptoms
- automated unit tests and code coverage analysis, integrated with CI/CD, are extremely useful tools in any project