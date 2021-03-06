USE [EmployeeManagement]
GO
/****** Object:  Table [dbo].[Employee]    Script Date: 10/28/2020 4:25:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Employee](
	[empID] [nvarchar](50) NOT NULL,
	[fullName] [nvarchar](50) NOT NULL,
	[phone] [nvarchar](10) NOT NULL,
	[email] [nvarchar](30) NOT NULL,
	[address] [nvarchar](50) NOT NULL,
	[dateOfBirth] [bigint] NOT NULL,
	[isDelete] [bit] NOT NULL,
 CONSTRAINT [PK_Employee] PRIMARY KEY CLUSTERED 
(
	[empID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
