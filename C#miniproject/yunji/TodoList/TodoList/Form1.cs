using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;

namespace TodoList
{
    public partial class Form1 : Form
    {
        private SaveFileDialog saveFileDialog;
        private OpenFileDialog openFileDialog;

        public Form1()
        {
            InitializeComponent();
        }

        private void saveTextFile()
        {
            saveFileDialog = new SaveFileDialog();

            saveFileDialog.InitialDirectory = "C:"; //초기 dir
            saveFileDialog.Title = "저장하기";
            saveFileDialog.DefaultExt = "txt"; //기본 확장명
            saveFileDialog.Filter = "Txt files(*.txt)|*.txt";
        }

        private void openTextFile()
        {
            openFileDialog = new OpenFileDialog();

            openFileDialog.InitialDirectory = "C:";
            openFileDialog.Title = "불러오기";
            openFileDialog.DefaultExt = "txt";
            openFileDialog.Filter = "Txt files(*.txt)|*.txt";
        }

        private void buttonAdd_Click(object sender, EventArgs e)
        {
            Form2 newForm = new Form2();

            if (newForm.ShowDialog() == DialogResult.OK)
            {
                checkedListBoxToDo.Items.Add(newForm.addThing);
            }
        }

        private void buttonDel_Click(object sender, EventArgs e)
        {
            for (int i = checkedListBoxToDo.Items.Count - 1; i >= 0; i--)
            {
                if (checkedListBoxToDo.GetItemChecked(i))
                {
                    checkedListBoxToDo.Items.Remove(checkedListBoxToDo.Items[i]);
                }
            }
        }

        private void buttonSave_Click(object sender, EventArgs e)
        {
            saveTextFile();

            if (saveFileDialog.ShowDialog() == DialogResult.OK)
            {
                StreamWriter writer = new StreamWriter(saveFileDialog.OpenFile());
                writer.WriteLine(textBoxM.Text + "월 " + textBoxD.Text + "일 " + " TodoList");

                for (int i = 0; i <= checkedListBoxToDo.Items.Count - 1; i++)
                {
                    if (checkedListBoxToDo.GetItemChecked(i))
                    {
                        writer.WriteLine(i + 1 + ". " + checkedListBoxToDo.Items[i].ToString());
                    }

                }

                writer.Dispose();
                writer.Close();
            }
        }

        private void buttonOpen_Click(object sender, EventArgs e)
        {
            List<string> lines = new List<string>();
            string text;

            openTextFile();

            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                textBoxM.Text = "";
                textBoxD.Text = "";

                for (int i = checkedListBoxToDo.Items.Count - 1; i >= 0; i--)
                {
                    checkedListBoxToDo.Items.Remove(checkedListBoxToDo.Items[i]);
                }

                StreamReader reader = new StreamReader(openFileDialog.OpenFile());
                while ((text = reader.ReadLine()) != null)
                {
                    lines.Add(text);
                }

                for (int i = 0; i < lines.Count; i++)
                {
                    if (i == 0)
                    {
                        textBoxM.Text = lines[i].Split(' ')[0].Split('월')[0];
                        textBoxD.Text = lines[i].Split(' ')[1].Split('일')[0];
                    }
                    else
                    {
                        checkedListBoxToDo.Items.Add(lines[i].Split('.')[1]);
                    }
                }

                reader.Dispose();
                reader.Close();

            }
        }

        private void checkBoxAll_CheckedChanged(object sender, EventArgs e)
        {
            for (int i = checkedListBoxToDo.Items.Count - 1; i >= 0; i--)
            {
                checkedListBoxToDo.SetItemChecked(i, checkBoxAll.Checked);
            }
        }

        private void buttonUp_Click(object sender, EventArgs e)
        {
            int selectedIndex = checkedListBoxToDo.SelectedIndex;
            if (selectedIndex > 0)
            {
                checkedListBoxToDo.Items.Insert(selectedIndex - 1, checkedListBoxToDo.Items[selectedIndex]);
                checkedListBoxToDo.Items.RemoveAt(selectedIndex + 1);
                checkedListBoxToDo.SelectedIndex = selectedIndex - 1;
            }
        }

        private void buttonDown_Click(object sender, EventArgs e)
        {
            int selectedIndex = checkedListBoxToDo.SelectedIndex;
            if (selectedIndex < checkedListBoxToDo.Items.Count - 1 && selectedIndex != -1)
            {
                checkedListBoxToDo.Items.Insert(selectedIndex + 2, checkedListBoxToDo.Items[selectedIndex]);
                checkedListBoxToDo.Items.RemoveAt(selectedIndex);
                checkedListBoxToDo.SelectedIndex = selectedIndex + 1;
            }
        }
    }
}
