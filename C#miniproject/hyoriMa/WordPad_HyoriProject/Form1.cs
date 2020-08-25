using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.IO;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using DevComponents.DotNetBar;

namespace WordPad_HyoriProject
{
    public partial class MyForm : Office2007RibbonForm
    {
        private List<string> fontList;
        private List<int> fontSizeList;

        private RichTextBox richTextBox;
        private ComboBoxItem fontComboBox;
        private ComboBoxItem fontSizeComboBox;

        public MyForm()
        {
            InitializeComponent();

        }

        //When Form is loading..
        private void Form1_Load_1(object sender, EventArgs e)
        {
            ////////////////////////////////////////////////////
            richTextBox = this.richTextBox1;
            fontComboBox = this.comboBoxItem1;
            fontSizeComboBox = this.comboBoxItem2;
            ////////////////////////////////////////////////////


            MyFont font = new MyFont();
            fontList = font.getFontFamilies();
            foreach (string item in fontList)
            {
                fontComboBox.Items.Add(item);
            }

            fontSizeList = font.getFontSize();
            foreach (int item in fontSizeList)
            {
                fontSizeComboBox.Items.Add(item.ToString());
            }

            //Get Default Richtextbox's fontName and fontSize 
            string fontName = richTextBox.SelectionFont.Name;
            int fontNameIndex = fontComboBox.Items.IndexOf(fontName);
            fontComboBox.SelectedIndex = fontNameIndex;

            float fontSize = richTextBox.SelectionFont.Size;
            int fontSizeIndex = fontSizeComboBox.Items.IndexOf(fontSize.ToString());
            fontSizeComboBox.SelectedIndex = fontSizeIndex;


        }

        //fontName & fontSize 
        class MyFont
        {
            private List<string> fontFamilies = new List<string>();
            private List<int> fontsizeList = new List<int>();

            //Get All font items
            public List<string> getFontFamilies()
            {
                fontFamilies = new List<string>();
                foreach (FontFamily family in FontFamily.Families)
                {
                    fontFamilies.Add(family.Name);
                }
                return fontFamilies;
            }

            //Get All fontsize items
            public List<int> getFontSize()
            {
                fontsizeList = new List<int>();
                for (int i = 0; i < 100; i++)
                {
                    fontsizeList.Add(i + 1);
                }
                return fontsizeList;
            }


        }

        //Font Color (글자색)
        private void colorPickerDropDown1_SelectedColorChanged(object sender, EventArgs e)
        {
            ColorPickerDropDown cpd = sender as ColorPickerDropDown;
            Color color = Color.FromArgb(cpd.SelectedColor.ToArgb());
            richTextBox.SelectionColor = color;
        }

        //FontName comboBox Changed (폰트 스타일)
        private void comboBoxItem1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (richTextBox.SelectionFont != null)
            {
                string selectedFontItem = fontComboBox.SelectedItem.ToString();
                Font currentFont = richTextBox.SelectionFont;

                if (buttonItem14.Checked == true)
                {
                    richTextBox.SelectionFont = new Font(selectedFontItem, currentFont.Size, FontStyle.Bold);
                }
                else
                {
                    richTextBox.SelectionFont = new Font(selectedFontItem, currentFont.Size, FontStyle.Regular);
                }
            }

        }

        //FontSize comboBox Changed (글자 크기)
        private void comboBoxItem2_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (richTextBox.SelectionFont != null)
            {
                string selectedFontItem = fontSizeComboBox.SelectedItem.ToString();
                Font currentFont = richTextBox.SelectionFont;

                if (buttonItem14.Checked == true)
                {
                    richTextBox.SelectionFont = new Font(currentFont.FontFamily, Int32.Parse(selectedFontItem), FontStyle.Bold);
                }
                else
                {
                    richTextBox.SelectionFont = new Font(currentFont.FontFamily, Int32.Parse(selectedFontItem), FontStyle.Regular);
                }
            }

        }

        //FontBoldButton Checked or Not Checked (글자 굵게)
        private void fontBold_CheckedChanged(object sender, EventArgs e)
        {
            if (richTextBox.SelectionFont != null)
            {
                Font currentFont = richTextBox.SelectionFont;

                if (buttonItem14.Checked == true)
                {
                    richTextBox.SelectionFont = new Font(currentFont.FontFamily, currentFont.Size, FontStyle.Bold);
                }
                else
                {
                    richTextBox.SelectionFont = new Font(currentFont.FontFamily, currentFont.Size, FontStyle.Regular);
                }

            }
        }

        //Text Alignment (정렬)
        private void leftAlignButton_Clicked(object sender, EventArgs e)
        {
            richTextBox.SelectionAlignment = HorizontalAlignment.Left;
        }

        private void middleAlignButton_Clicked(object sender, EventArgs e)
        {
            richTextBox.SelectionAlignment = HorizontalAlignment.Center;
        }

        private void rightAlignButton_Clicked(object sender, EventArgs e)
        {
            richTextBox.SelectionAlignment = HorizontalAlignment.Right;
        }

        //File Open (열기)
        private void openFileButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "모든 워드패드 문서(*.rtf; *.docx; *.odt; *.txt)|*.rtf; *.docx; *.odt; *.txt";

            //click "open" button
            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                StreamReader reader = new StreamReader(openFileDialog.FileName);
                richTextBox.Text = reader.ReadToEnd();

                string fileName = Path.GetFileName(openFileDialog.FileName);
                this.Text = fileName;

                reader.Close();

                //showing recent files
                ButtonItem buttonItem = new ButtonItem();
                buttonItem.Text = fileName;
                buttonItem.Click += ButtonClickHandler;
                itemContainer.SubItems.Add(buttonItem,0);
            }

        }

        //button 누를시 file 출력 
        private void ButtonClickHandler(object sender, EventArgs e)
        {
            string fileName = sender.ToString();
            StreamReader reader = new StreamReader(Path.GetFullPath(fileName));
            richTextBox.Text = reader.ReadToEnd();

            this.Text = fileName;

            reader.Close();
        }

        //Save (저장)
        private void saveButton_Click(object sender, EventArgs e)
        {
            DialogResult result;
            result = MessageBox.Show("저장하겠습니까?", "Save", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
            if (result == DialogResult.Yes)
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog();
                saveFileDialog.Filter = "txt files (*.txt)|*.txt|All files (*.*)|*.*";
                saveFileDialog.InitialDirectory = "C:";

                if (saveFileDialog.ShowDialog() == DialogResult.OK)
                {
                    File.WriteAllText(saveFileDialog.FileName, richTextBox.Text);
                }
            }
        }

        //Print (프린트)
        private void printButton_Click(object sender, EventArgs e)
        {
            PrintDialog printDialog = new PrintDialog();
            printDialog.ShowDialog();
        }


        //Exit (끝내기)
        private void exitButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void buttonItem4_Click(object sender, EventArgs e)
        {

        }
    }
}
