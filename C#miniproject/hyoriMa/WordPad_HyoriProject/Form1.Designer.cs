namespace WordPad_HyoriProject
{
    partial class MyForm
    {
        /// <summary>
        /// 필수 디자이너 변수입니다.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 사용 중인 모든 리소스를 정리합니다.
        /// </summary>
        /// <param name="disposing">관리되는 리소스를 삭제해야 하면 true이고, 그렇지 않으면 false입니다.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form 디자이너에서 생성한 코드

        /// <summary>
        /// 디자이너 지원에 필요한 메서드입니다. 
        /// 이 메서드의 내용을 코드 편집기로 수정하지 마세요.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MyForm));
            this.ribbonControl1 = new DevComponents.DotNetBar.RibbonControl();
            this.ribbonPanel1 = new DevComponents.DotNetBar.RibbonPanel();
            this.ribbonBar1 = new DevComponents.DotNetBar.RibbonBar();
            this.leftAlign = new DevComponents.DotNetBar.ButtonItem();
            this.middleAlign = new DevComponents.DotNetBar.ButtonItem();
            this.rightAlign = new DevComponents.DotNetBar.ButtonItem();
            this.Font = new DevComponents.DotNetBar.RibbonBar();
            this.itemContainer5 = new DevComponents.DotNetBar.ItemContainer();
            this.itemContainer7 = new DevComponents.DotNetBar.ItemContainer();
            this.fontComboBox = new DevComponents.DotNetBar.ComboBoxItem();
            this.fontSizeComboBox = new DevComponents.DotNetBar.ComboBoxItem();
            this.itemContainer8 = new DevComponents.DotNetBar.ItemContainer();
            this.buttonItem14 = new DevComponents.DotNetBar.ButtonItem();
            this.buttonItem15 = new DevComponents.DotNetBar.ButtonItem();
            this.buttonItem16 = new DevComponents.DotNetBar.ButtonItem();
            this.colorPickerDropDown = new DevComponents.DotNetBar.ColorPickerDropDown();
            this.applicationButton1 = new DevComponents.DotNetBar.ApplicationButton();
            this.itemContainer1 = new DevComponents.DotNetBar.ItemContainer();
            this.itemContainer2 = new DevComponents.DotNetBar.ItemContainer();
            this.itemContainer3 = new DevComponents.DotNetBar.ItemContainer();
            this.buttonItem2 = new DevComponents.DotNetBar.ButtonItem();
            this.openFileButton = new DevComponents.DotNetBar.ButtonItem();
            this.saveButton = new DevComponents.DotNetBar.ButtonItem();
            this.printButton = new DevComponents.DotNetBar.ButtonItem();
            this.galleryContainer1 = new DevComponents.DotNetBar.GalleryContainer();
            this.labelItem8 = new DevComponents.DotNetBar.LabelItem();
            this.itemContainer = new DevComponents.DotNetBar.ItemContainer();
            this.itemContainer4 = new DevComponents.DotNetBar.ItemContainer();
            this.exitButton = new DevComponents.DotNetBar.ButtonItem();
            this.ribbonTabItem1 = new DevComponents.DotNetBar.RibbonTabItem();
            this.styleManager1 = new DevComponents.DotNetBar.StyleManager(this.components);
            this.richTextBox = new System.Windows.Forms.RichTextBox();
            this.맑은고딕 = new DevComponents.Editors.ComboItem();
            this.itemContainer6 = new DevComponents.DotNetBar.ItemContainer();
            this.ribbonControl1.SuspendLayout();
            this.ribbonPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // ribbonControl1
            // 
            // 
            // 
            // 
            this.ribbonControl1.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.ribbonControl1.CaptionVisible = true;
            this.ribbonControl1.Controls.Add(this.ribbonPanel1);
            this.ribbonControl1.Dock = System.Windows.Forms.DockStyle.Top;
            this.ribbonControl1.Items.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.applicationButton1,
            this.ribbonTabItem1});
            this.ribbonControl1.KeyTipsFont = new System.Drawing.Font("Tahoma", 7F);
            this.ribbonControl1.Location = new System.Drawing.Point(5, 1);
            this.ribbonControl1.Name = "ribbonControl1";
            this.ribbonControl1.Padding = new System.Windows.Forms.Padding(0, 0, 0, 3);
            this.ribbonControl1.Size = new System.Drawing.Size(790, 167);
            this.ribbonControl1.Style = DevComponents.DotNetBar.eDotNetBarStyle.StyleManagerControlled;
            this.ribbonControl1.SystemText.MaximizeRibbonText = "&Maximize the Ribbon";
            this.ribbonControl1.SystemText.MinimizeRibbonText = "Mi&nimize the Ribbon";
            this.ribbonControl1.SystemText.QatAddItemText = "&Add to Quick Access Toolbar";
            this.ribbonControl1.SystemText.QatCustomizeMenuLabel = "<b>Customize Quick Access Toolbar</b>";
            this.ribbonControl1.SystemText.QatCustomizeText = "&Customize Quick Access Toolbar...";
            this.ribbonControl1.SystemText.QatDialogAddButton = "&Add >>";
            this.ribbonControl1.SystemText.QatDialogCancelButton = "Cancel";
            this.ribbonControl1.SystemText.QatDialogCaption = "Customize Quick Access Toolbar";
            this.ribbonControl1.SystemText.QatDialogCategoriesLabel = "&Choose commands from:";
            this.ribbonControl1.SystemText.QatDialogOkButton = "OK";
            this.ribbonControl1.SystemText.QatDialogPlacementCheckbox = "&Place Quick Access Toolbar below the Ribbon";
            this.ribbonControl1.SystemText.QatDialogRemoveButton = "&Remove";
            this.ribbonControl1.SystemText.QatPlaceAboveRibbonText = "&Place Quick Access Toolbar above the Ribbon";
            this.ribbonControl1.SystemText.QatPlaceBelowRibbonText = "&Place Quick Access Toolbar below the Ribbon";
            this.ribbonControl1.SystemText.QatRemoveItemText = "&Remove from Quick Access Toolbar";
            this.ribbonControl1.TabGroupHeight = 14;
            this.ribbonControl1.TabIndex = 0;
            this.ribbonControl1.Text = "WordPad";
            // 
            // ribbonPanel1
            // 
            this.ribbonPanel1.ColorSchemeStyle = DevComponents.DotNetBar.eDotNetBarStyle.StyleManagerControlled;
            this.ribbonPanel1.Controls.Add(this.ribbonBar1);
            this.ribbonPanel1.Controls.Add(this.Font);
            this.ribbonPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.ribbonPanel1.Font = new System.Drawing.Font("굴림", 9F);
            this.ribbonPanel1.Location = new System.Drawing.Point(0, 53);
            this.ribbonPanel1.Name = "ribbonPanel1";
            this.ribbonPanel1.Padding = new System.Windows.Forms.Padding(3, 0, 3, 3);
            this.ribbonPanel1.Size = new System.Drawing.Size(790, 111);
            // 
            // 
            // 
            this.ribbonPanel1.Style.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.ribbonPanel1.StyleMouseDown.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.ribbonPanel1.StyleMouseOver.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.ribbonPanel1.TabIndex = 1;
            // 
            // ribbonBar1
            // 
            this.ribbonBar1.AutoOverflowEnabled = true;
            // 
            // 
            // 
            this.ribbonBar1.BackgroundMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.ribbonBar1.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.ribbonBar1.ContainerControlProcessDialogKey = true;
            this.ribbonBar1.DragDropSupport = true;
            this.ribbonBar1.Font = new System.Drawing.Font("굴림", 8F);
            this.ribbonBar1.HorizontalItemAlignment = DevComponents.DotNetBar.eHorizontalItemsAlignment.Center;
            this.ribbonBar1.Items.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.leftAlign,
            this.middleAlign,
            this.rightAlign});
            this.ribbonBar1.Location = new System.Drawing.Point(197, 4);
            this.ribbonBar1.Name = "ribbonBar1";
            this.ribbonBar1.Size = new System.Drawing.Size(128, 104);
            this.ribbonBar1.Style = DevComponents.DotNetBar.eDotNetBarStyle.StyleManagerControlled;
            this.ribbonBar1.TabIndex = 1;
            this.ribbonBar1.Text = "Alignment";
            // 
            // 
            // 
            this.ribbonBar1.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.ribbonBar1.TitleStyleMouseOver.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.ribbonBar1.VerticalItemAlignment = DevComponents.DotNetBar.eVerticalItemsAlignment.Middle;
            // 
            // leftAlign
            // 
            this.leftAlign.Image = ((System.Drawing.Image)(resources.GetObject("leftAlign.Image")));
            this.leftAlign.Name = "leftAlign";
            this.leftAlign.SubItemsExpandWidth = 14;
            this.leftAlign.Text = "left";
            this.leftAlign.Click += new System.EventHandler(this.leftAlignButton_Clicked);
            // 
            // middleAlign
            // 
            this.middleAlign.Image = ((System.Drawing.Image)(resources.GetObject("middleAlign.Image")));
            this.middleAlign.Name = "middleAlign";
            this.middleAlign.SubItemsExpandWidth = 14;
            this.middleAlign.Text = "middle";
            this.middleAlign.Click += new System.EventHandler(this.middleAlignButton_Clicked);
            // 
            // rightAlign
            // 
            this.rightAlign.Image = ((System.Drawing.Image)(resources.GetObject("rightAlign.Image")));
            this.rightAlign.Name = "rightAlign";
            this.rightAlign.SubItemsExpandWidth = 14;
            this.rightAlign.Text = "right";
            this.rightAlign.Click += new System.EventHandler(this.rightAlignButton_Clicked);
            // 
            // Font
            // 
            this.Font.AutoOverflowEnabled = true;
            // 
            // 
            // 
            this.Font.BackgroundMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.Font.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.Font.ContainerControlProcessDialogKey = true;
            this.Font.Dock = System.Windows.Forms.DockStyle.Left;
            this.Font.DragDropSupport = true;
            this.Font.Font = new System.Drawing.Font("굴림", 8F);
            this.Font.HorizontalItemAlignment = DevComponents.DotNetBar.eHorizontalItemsAlignment.Center;
            this.Font.Items.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.itemContainer5});
            this.Font.Location = new System.Drawing.Point(3, 0);
            this.Font.Name = "Font";
            this.Font.Size = new System.Drawing.Size(188, 108);
            this.Font.Style = DevComponents.DotNetBar.eDotNetBarStyle.StyleManagerControlled;
            this.Font.TabIndex = 0;
            this.Font.Text = "Font";
            // 
            // 
            // 
            this.Font.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.Font.TitleStyleMouseOver.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // itemContainer5
            // 
            // 
            // 
            // 
            this.itemContainer5.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer5.HorizontalItemAlignment = DevComponents.DotNetBar.eHorizontalItemsAlignment.Center;
            this.itemContainer5.LayoutOrientation = DevComponents.DotNetBar.eOrientation.Vertical;
            this.itemContainer5.Name = "itemContainer5";
            this.itemContainer5.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.itemContainer7,
            this.itemContainer8});
            // 
            // 
            // 
            this.itemContainer5.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer5.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer5.VerticalItemAlignment = DevComponents.DotNetBar.eVerticalItemsAlignment.Middle;
            // 
            // itemContainer7
            // 
            // 
            // 
            // 
            this.itemContainer7.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer7.HorizontalItemAlignment = DevComponents.DotNetBar.eHorizontalItemsAlignment.Center;
            this.itemContainer7.Name = "itemContainer7";
            this.itemContainer7.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.fontComboBox,
            this.fontSizeComboBox});
            // 
            // 
            // 
            this.itemContainer7.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer7.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // fontComboBox
            // 
            this.fontComboBox.ComboWidth = 100;
            this.fontComboBox.DropDownHeight = 106;
            this.fontComboBox.DropDownWidth = 100;
            this.fontComboBox.Name = "fontComboBox";
            this.fontComboBox.Text = "comboBoxItem1";
            this.fontComboBox.SelectedIndexChanged += new System.EventHandler(this.fontNameComboBox_SelectedIndexChanged);
            // 
            // fontSizeComboBox
            // 
            this.fontSizeComboBox.DropDownHeight = 106;
            this.fontSizeComboBox.Name = "fontSizeComboBox";
            this.fontSizeComboBox.Text = "comboBoxItem2";
            this.fontSizeComboBox.SelectedIndexChanged += new System.EventHandler(this.fontSizeComboBox_SelectedIndexChanged);
            // 
            // itemContainer8
            // 
            // 
            // 
            // 
            this.itemContainer8.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer8.Name = "itemContainer8";
            this.itemContainer8.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.buttonItem14,
            this.buttonItem15,
            this.buttonItem16,
            this.colorPickerDropDown});
            // 
            // 
            // 
            this.itemContainer8.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer8.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // buttonItem14
            // 
            this.buttonItem14.AutoCheckOnClick = true;
            this.buttonItem14.Name = "buttonItem14";
            this.buttonItem14.Text = "<b>가</b>";
            this.buttonItem14.CheckedChanged += new System.EventHandler(this.fontBold_CheckedChanged);
            // 
            // buttonItem15
            // 
            this.buttonItem15.AutoCheckOnClick = true;
            this.buttonItem15.Name = "buttonItem15";
            this.buttonItem15.Text = "<i>가</i>";
            // 
            // buttonItem16
            // 
            this.buttonItem16.AutoCheckOnClick = true;
            this.buttonItem16.Name = "buttonItem16";
            this.buttonItem16.Text = "<u>가</u>";
            // 
            // colorPickerDropDown
            // 
            this.colorPickerDropDown.Name = "colorPickerDropDown";
            this.colorPickerDropDown.Text = "<u><b>A</b></u>";
            this.colorPickerDropDown.SelectedColorChanged += new System.EventHandler(this.colorPickerDropDown1_SelectedColorChanged);
            // 
            // applicationButton1
            // 
            this.applicationButton1.AutoExpandOnClick = true;
            this.applicationButton1.CanCustomize = false;
            this.applicationButton1.HotTrackingStyle = DevComponents.DotNetBar.eHotTrackingStyle.Image;
            this.applicationButton1.Image = ((System.Drawing.Image)(resources.GetObject("applicationButton1.Image")));
            this.applicationButton1.ImageFixedSize = new System.Drawing.Size(16, 16);
            this.applicationButton1.ImagePaddingHorizontal = 0;
            this.applicationButton1.ImagePaddingVertical = 0;
            this.applicationButton1.Name = "applicationButton1";
            this.applicationButton1.ShowSubItems = false;
            this.applicationButton1.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.itemContainer1});
            this.applicationButton1.Text = "&File";
            // 
            // itemContainer1
            // 
            // 
            // 
            // 
            this.itemContainer1.BackgroundStyle.Class = "RibbonFileMenuContainer";
            this.itemContainer1.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer1.LayoutOrientation = DevComponents.DotNetBar.eOrientation.Vertical;
            this.itemContainer1.Name = "itemContainer1";
            this.itemContainer1.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.itemContainer2,
            this.itemContainer4});
            // 
            // 
            // 
            this.itemContainer1.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer1.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // itemContainer2
            // 
            // 
            // 
            // 
            this.itemContainer2.BackgroundStyle.Class = "RibbonFileMenuTwoColumnContainer";
            this.itemContainer2.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer2.ItemSpacing = 0;
            this.itemContainer2.Name = "itemContainer2";
            this.itemContainer2.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.itemContainer3,
            this.galleryContainer1});
            // 
            // 
            // 
            this.itemContainer2.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer2.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // itemContainer3
            // 
            // 
            // 
            // 
            this.itemContainer3.BackgroundStyle.Class = "RibbonFileMenuColumnOneContainer";
            this.itemContainer3.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer3.LayoutOrientation = DevComponents.DotNetBar.eOrientation.Vertical;
            this.itemContainer3.MinimumSize = new System.Drawing.Size(120, 0);
            this.itemContainer3.Name = "itemContainer3";
            this.itemContainer3.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.buttonItem2,
            this.openFileButton,
            this.saveButton,
            this.printButton});
            // 
            // 
            // 
            this.itemContainer3.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer3.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // buttonItem2
            // 
            this.buttonItem2.ButtonStyle = DevComponents.DotNetBar.eButtonStyle.ImageAndText;
            this.buttonItem2.Image = ((System.Drawing.Image)(resources.GetObject("buttonItem2.Image")));
            this.buttonItem2.Name = "buttonItem2";
            this.buttonItem2.SubItemsExpandWidth = 24;
            this.buttonItem2.Text = "&New";
            // 
            // openFileButton
            // 
            this.openFileButton.ButtonStyle = DevComponents.DotNetBar.eButtonStyle.ImageAndText;
            this.openFileButton.Image = ((System.Drawing.Image)(resources.GetObject("openFileButton.Image")));
            this.openFileButton.Name = "openFileButton";
            this.openFileButton.SubItemsExpandWidth = 24;
            this.openFileButton.Text = "&Open...";
            this.openFileButton.Click += new System.EventHandler(this.openFileButton_Click);
            // 
            // saveButton
            // 
            this.saveButton.ButtonStyle = DevComponents.DotNetBar.eButtonStyle.ImageAndText;
            this.saveButton.Image = ((System.Drawing.Image)(resources.GetObject("saveButton.Image")));
            this.saveButton.Name = "saveButton";
            this.saveButton.SubItemsExpandWidth = 24;
            this.saveButton.Text = "&Save...";
            this.saveButton.Click += new System.EventHandler(this.saveButton_Click);
            // 
            // printButton
            // 
            this.printButton.ButtonStyle = DevComponents.DotNetBar.eButtonStyle.ImageAndText;
            this.printButton.Image = ((System.Drawing.Image)(resources.GetObject("printButton.Image")));
            this.printButton.Name = "printButton";
            this.printButton.SubItemsExpandWidth = 24;
            this.printButton.Text = "&Print...";
            this.printButton.Click += new System.EventHandler(this.printButton_Click);
            // 
            // galleryContainer1
            // 
            // 
            // 
            // 
            this.galleryContainer1.BackgroundStyle.Class = "RibbonFileMenuColumnTwoContainer";
            this.galleryContainer1.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.galleryContainer1.EnableGalleryPopup = false;
            this.galleryContainer1.LayoutOrientation = DevComponents.DotNetBar.eOrientation.Vertical;
            this.galleryContainer1.MinimumSize = new System.Drawing.Size(180, 240);
            this.galleryContainer1.MultiLine = false;
            this.galleryContainer1.Name = "galleryContainer1";
            this.galleryContainer1.PopupUsesStandardScrollbars = false;
            this.galleryContainer1.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.labelItem8,
            this.itemContainer});
            // 
            // 
            // 
            this.galleryContainer1.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.galleryContainer1.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // labelItem8
            // 
            this.labelItem8.BorderSide = DevComponents.DotNetBar.eBorderSide.Bottom;
            this.labelItem8.BorderType = DevComponents.DotNetBar.eBorderType.Etched;
            this.labelItem8.CanCustomize = false;
            this.labelItem8.ForeColor = System.Drawing.SystemColors.ControlText;
            this.labelItem8.Name = "labelItem8";
            this.labelItem8.PaddingBottom = 2;
            this.labelItem8.PaddingTop = 2;
            this.labelItem8.Stretch = true;
            this.labelItem8.Text = "Recent Documents";
            // 
            // itemContainer
            // 
            // 
            // 
            // 
            this.itemContainer.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer.LayoutOrientation = DevComponents.DotNetBar.eOrientation.Vertical;
            this.itemContainer.Name = "itemContainer";
            // 
            // 
            // 
            this.itemContainer.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // itemContainer4
            // 
            // 
            // 
            // 
            this.itemContainer4.BackgroundStyle.Class = "RibbonFileMenuBottomContainer";
            this.itemContainer4.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer4.HorizontalItemAlignment = DevComponents.DotNetBar.eHorizontalItemsAlignment.Right;
            this.itemContainer4.Name = "itemContainer4";
            this.itemContainer4.SubItems.AddRange(new DevComponents.DotNetBar.BaseItem[] {
            this.exitButton});
            // 
            // 
            // 
            this.itemContainer4.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer4.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // exitButton
            // 
            this.exitButton.ButtonStyle = DevComponents.DotNetBar.eButtonStyle.ImageAndText;
            this.exitButton.ColorTable = DevComponents.DotNetBar.eButtonColor.OrangeWithBackground;
            this.exitButton.Image = ((System.Drawing.Image)(resources.GetObject("exitButton.Image")));
            this.exitButton.Name = "exitButton";
            this.exitButton.SubItemsExpandWidth = 24;
            this.exitButton.Text = "E&xit";
            this.exitButton.Click += new System.EventHandler(this.exitButton_Click);
            // 
            // ribbonTabItem1
            // 
            this.ribbonTabItem1.Checked = true;
            this.ribbonTabItem1.Name = "ribbonTabItem1";
            this.ribbonTabItem1.Panel = this.ribbonPanel1;
            this.ribbonTabItem1.Text = "Home";
            // 
            // styleManager1
            // 
            this.styleManager1.ManagerStyle = DevComponents.DotNetBar.eStyle.Office2010Blue;
            this.styleManager1.MetroColorParameters = new DevComponents.DotNetBar.Metro.ColorTables.MetroColorGeneratorParameters(System.Drawing.Color.White, System.Drawing.Color.FromArgb(((int)(((byte)(43)))), ((int)(((byte)(87)))), ((int)(((byte)(154))))));
            // 
            // richTextBox
            // 
            this.richTextBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.richTextBox.Location = new System.Drawing.Point(5, 168);
            this.richTextBox.Name = "richTextBox";
            this.richTextBox.Size = new System.Drawing.Size(790, 280);
            this.richTextBox.TabIndex = 1;
            this.richTextBox.Text = "";
            // 
            // 맑은고딕
            // 
            this.맑은고딕.Text = "맑은고딕";
            // 
            // itemContainer6
            // 
            // 
            // 
            // 
            this.itemContainer6.BackgroundStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            this.itemContainer6.LayoutOrientation = DevComponents.DotNetBar.eOrientation.Vertical;
            this.itemContainer6.Name = "itemContainer6";
            // 
            // 
            // 
            this.itemContainer6.TitleMouseOverStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // 
            // 
            this.itemContainer6.TitleStyle.CornerType = DevComponents.DotNetBar.eCornerType.Square;
            // 
            // MyForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.richTextBox);
            this.Controls.Add(this.ribbonControl1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "MyForm";
            this.Text = "WordPad";
            this.Load += new System.EventHandler(this.MyForm_Load);
            this.ribbonControl1.ResumeLayout(false);
            this.ribbonControl1.PerformLayout();
            this.ribbonPanel1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private DevComponents.DotNetBar.RibbonControl ribbonControl1;
        private DevComponents.DotNetBar.RibbonPanel ribbonPanel1;
        private DevComponents.DotNetBar.ApplicationButton applicationButton1;
        private DevComponents.DotNetBar.ItemContainer itemContainer1;
        private DevComponents.DotNetBar.ItemContainer itemContainer2;
        private DevComponents.DotNetBar.ItemContainer itemContainer3;
        private DevComponents.DotNetBar.ButtonItem buttonItem2;
        private DevComponents.DotNetBar.ButtonItem openFileButton;
        private DevComponents.DotNetBar.ButtonItem saveButton;
        private DevComponents.DotNetBar.ButtonItem printButton;
        private DevComponents.DotNetBar.GalleryContainer galleryContainer1;
        private DevComponents.DotNetBar.LabelItem labelItem8;
        private DevComponents.DotNetBar.ItemContainer itemContainer4;
        private DevComponents.DotNetBar.ButtonItem exitButton;
        private DevComponents.DotNetBar.RibbonTabItem ribbonTabItem1;
        private DevComponents.DotNetBar.StyleManager styleManager1;
        private System.Windows.Forms.RichTextBox richTextBox;
        private DevComponents.DotNetBar.RibbonBar ribbonBar1;
        private DevComponents.Editors.ComboItem 맑은고딕;
        private new DevComponents.DotNetBar.RibbonBar Font;
        private DevComponents.DotNetBar.ItemContainer itemContainer6;
        private DevComponents.DotNetBar.ItemContainer itemContainer5;
        private DevComponents.DotNetBar.ItemContainer itemContainer7;
        private DevComponents.DotNetBar.ComboBoxItem fontComboBox;
        private DevComponents.DotNetBar.ComboBoxItem fontSizeComboBox;
        private DevComponents.DotNetBar.ColorPickerDropDown colorPickerDropDown;
        private DevComponents.DotNetBar.ItemContainer itemContainer8;
        private DevComponents.DotNetBar.ButtonItem buttonItem14;
        private DevComponents.DotNetBar.ButtonItem buttonItem15;
        private DevComponents.DotNetBar.ButtonItem buttonItem16;
        private DevComponents.DotNetBar.ButtonItem leftAlign;
        private DevComponents.DotNetBar.ButtonItem middleAlign;
        private DevComponents.DotNetBar.ButtonItem rightAlign;
        private DevComponents.DotNetBar.ItemContainer itemContainer;
    }
}

