USE [ItemManagement]
GO
/****** Object:  Table [dbo].[tblItems]    Script Date: 9/30/2020 7:32:13 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tblItems](
	[itemCode] [varchar](10) NOT NULL,
	[itemName] [nvarchar](50) NOT NULL,
	[unit] [varchar](50) NOT NULL,
	[price] [float] NOT NULL,
	[supplying] [bit] NOT NULL,
	[supCode] [varchar](10) NOT NULL,
 CONSTRAINT [PK_tblItems] PRIMARY KEY CLUSTERED 
(
	[itemCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[tblSuppliers]    Script Date: 9/30/2020 7:32:13 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tblSuppliers](
	[supCode] [varchar](10) NOT NULL,
	[supName] [nvarchar](50) NOT NULL,
	[address] [nvarchar](50) NOT NULL,
	[collaborating] [bit] NOT NULL,
 CONSTRAINT [PK_tblSuppliers] PRIMARY KEY CLUSTERED 
(
	[supCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[tblUsers]    Script Date: 9/30/2020 7:32:13 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tblUsers](
	[userID] [varchar](10) NOT NULL,
	[fullName] [nvarchar](50) NOT NULL,
	[password] [varchar](50) NOT NULL,
	[status] [bit] NOT NULL,
 CONSTRAINT [PK_tblUsers] PRIMARY KEY CLUSTERED 
(
	[userID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[tblItems] ([itemCode], [itemName], [unit], [price], [supplying], [supCode]) VALUES (N'123', N'Kevin', N'123', 123, 1, N'S01')
INSERT [dbo].[tblItems] ([itemCode], [itemName], [unit], [price], [supplying], [supCode]) VALUES (N'456', N'Luxu', N'12', 100, 1, N'S02')
GO
INSERT [dbo].[tblSuppliers] ([supCode], [supName], [address], [collaborating]) VALUES (N'S01', N'Lucas', N'Macbook', 1)
INSERT [dbo].[tblSuppliers] ([supCode], [supName], [address], [collaborating]) VALUES (N'S02', N'Timm', N'Ipad', 0)
INSERT [dbo].[tblSuppliers] ([supCode], [supName], [address], [collaborating]) VALUES (N'S03', N'Michael', N'Iphone', 1)
GO
INSERT [dbo].[tblUsers] ([userID], [fullName], [password], [status]) VALUES (N'admin', N'admin123', N'admin', 1)
GO
ALTER TABLE [dbo].[tblItems]  WITH CHECK ADD  CONSTRAINT [FK_tblItems_tblSuppliers] FOREIGN KEY([supCode])
REFERENCES [dbo].[tblSuppliers] ([supCode])
GO
ALTER TABLE [dbo].[tblItems] CHECK CONSTRAINT [FK_tblItems_tblSuppliers]
GO
