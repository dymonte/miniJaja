Feature: Com
	Scenario: Running a minijaja program from the application
		Given I have a valid minijaja program that prints <value>
		When I execute the program
		Then The program should run without errors
		And The output should be <value>
		And The stack is empty
		And The heap is empty

		Examples:
			| value |
			| 42    |
			| 24    |
			| 123   |

	Scenario: Running a minijaja program from the application with issue
		Given I have a not valid minijaja program with <error> issue
		When I execute the program
		Then The program should be throw "<error>"

		Examples:
			| error  |
			| syntax |
			| memory |

	Scenario: Running a minijaja program with comments
		Given I have a valid minijaja program with comments that prints "<value>"
		When I execute the program
		Then The program should run without errors
		And The output should be "<value>"
		And The stack is empty
		And The heap is empty

		Examples:
			| value     |
			| print     |
			| something |
			| hey       |

	Scenario: Clearing the console after execute program
		Given I have a valid minijaja program that prints 42
		When I execute the program
		And I clear the console
		Then The program should run without errors
		And The output should be empty
		And The stack is empty
		And The heap is empty

	Scenario: Closing the app
		Given I have a valid minijaja program that prints 42
		When I quit the app
		Then I have a message box with this message : "Are you sure you want to quit?"

	#Scenario: Execute step by step the program
	#	Given I have a valid minijaja program that prints 42
	#	When I execute the program step by step
	#	Then I have step by step menu
	#	And I click next until the program ends
